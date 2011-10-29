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
import urllib
import os
from google.appengine.ext.webapp import template
	
class APIHandler(webapp.RequestHandler):

	def get(self):
		
		path = os.path.join(os.path.dirname(__file__), 'error.xml')
		template_values = {'error' : 'Error Message: This version of Worth Watching is no longer supported. Please go to the Android Market or Amazon Appstore to update this app.'}
		self.response.out.write(template.render(path, template_values))
			
class MainHandler(webapp.RequestHandler):
    def get(self):	
		path = os.path.join(os.path.dirname(__file__), 'index.html')
		self.response.out.write(template.render(path, None))
		
def main():
    application = webapp.WSGIApplication([('/lists/dvds/.*', APIHandler),('/lists/movies/.*', APIHandler),('/movies.json', APIHandler),('/movies/.*', APIHandler),('/.*', MainHandler)], debug=False)
    util.run_wsgi_app(application)

if __name__ == '__main__':
    main()
