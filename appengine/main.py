#!/usr/bin/env python
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
from google.appengine.ext import webapp
from google.appengine.ext.webapp import util
from google.appengine.api import urlfetch
import urllib
import os
import datetime
import re
from google.appengine.ext.webapp import template
from google.appengine.ext import db

class MovieQueryCache(db.Model):
  endpoint = db.StringProperty(multiline=False)
  json = db.TextProperty()
  date = db.DateTimeProperty(auto_now_add=True)
	
def txnUpdateMovieQueryCache(endpoint, json):
	q = db.GqlQuery("SELECT * FROM MovieQueryCache " +
					"WHERE endpoint = :1 " +
					"ORDER BY date DESC",
					endpoint)
	
	result = q.get()
	
	if result is None:
		result = MovieQueryCache(endpoint = endpoint, json = json)
	else:
		result.json = json
		result.date = datetime.datetime.now()
		
	result.put()
	
def removeParameter(query_string, parameter):
	return re.sub(r'(&%(p)s=[^&]+)|(%(p)s=[^&]+&)|(^%(p)s=[^&]+$)' % {'p' : parameter}, '', query_string.strip())
	
class APIHandler(webapp.RequestHandler):

	def get(self):
		
		rottenTomatoesAPI = 'http://api.rottentomatoes.com/api/public/v1.0'		
		apikey = 'cfuvchhe98amz6vd9qc2s8m7'

		#Check for app version and app store
		app_version = self.request.get("app_version").strip()
		app_store = self.request.get("app_store").strip()
			
		# if app_version == '':
			# path = os.path.join(os.path.dirname(__file__), 'error.xml')
			# template_values = {'error' : 'Error Message: No app version provided.'}
			# self.response.out.write(template.render(path, template_values))
			# return
			
		# if app_store == '':
			# path = os.path.join(os.path.dirname(__file__), 'error.xml')
			# template_values = {'error' : 'Error Message: No app store provided.'}
			# self.response.out.write(template.render(path, template_values))
			# return
		
		if app_version == '':
			# old
			rt_query_string = removeParameter(self.request.query_string, 'account')
		else:
			# remove the parameters that rotten tomatoes doesn't need
			rt_query_string = removeParameter(removeParameter(self.request.query_string, 'app_version'), 'app_store')

		if rt_query_string == '':
			endpoint = rottenTomatoesAPI + self.request.path
			endpointWithApiKey = rottenTomatoesAPI + self.request.path + '?apikey=' + apikey
		else:
			endpoint = rottenTomatoesAPI + self.request.path + '?' + rt_query_string
			endpointWithApiKey = rottenTomatoesAPI + self.request.path + '?apikey=' + apikey + '&' + rt_query_string
		
		#get query from cache
		cachedEntity = db.GqlQuery( "SELECT * FROM MovieQueryCache " +
									"WHERE endpoint = :1 AND date >= :2 " +
									"ORDER BY date DESC",
									endpoint, datetime.date.today()).get()

		if cachedEntity is None:
		
			result = urlfetch.fetch(url=endpointWithApiKey, payload=None, method=urlfetch.GET, headers={'Content-Type': 'text/javascript'})
			
			if result.status_code != 200:
				path = os.path.join(os.path.dirname(__file__), 'error.xml')
				template_values = {'error' : 'HTTP GET returned status %d' % (result.status_code)}
				self.response.out.write(template.render(path, template_values))
				return
			else:
				json = result.content
				
				#update cache
				txnUpdateMovieQueryCache(endpoint, json)
			
		else:
			json = cachedEntity.json
		
		self.response.headers['Content-Type'] = 'text/javascript'
		self.response.out.write(json)	
			
class MainHandler(webapp.RequestHandler):
    def get(self):	
		path = os.path.join(os.path.dirname(__file__), 'index.html')
		self.response.out.write(template.render(path, None))
		
def main():
    application = webapp.WSGIApplication([('/lists/dvds/.*', APIHandler),('/lists/movies/.*', APIHandler),('/movies.json', APIHandler),('/movies/.*', APIHandler),('/.*', MainHandler)], debug=False)
    util.run_wsgi_app(application)

if __name__ == '__main__':
    main()
