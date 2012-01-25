from google.appengine.ext.db import stats
from google.appengine.api import mail
from google.appengine.ext import db

class MovieQueryCache(db.Model):
  endpoint = db.StringProperty(multiline=False)
  json = db.TextProperty()
  date = db.DateTimeProperty(auto_now_add=True)
 
global_stat = stats.GlobalStat.all().get()
q = db.GqlQuery("SELECT __key__ FROM MovieQueryCache")
mysubject = '%.f entries - Worth Watching App Engine Stats' % q.count()
mybody = '%.2f of 0.50 GB'  % (global_stat.bytes/1073741824.0)
#print 'Total entities stored: %f' % global_stat.count

mail.send_mail(sender="Worth Watching App Engine <jami@moubry.com>",
              to="Jami Moubry <jami@moubry.com>",
              subject=mysubject,
              body=mybody)
		
		
if 	True:
	try:
		q = db.GqlQuery("SELECT __key__ FROM MovieQueryCache")
		mycount = q.count()
		while mycount > 0:
			q = db.GqlQuery("SELECT __key__ FROM MovieQueryCache")
			nycount = q.count()
			db.delete(q.fetch(200))
	except Exception, e:
		print 'error'
		pass
		
if False:
	try:
		cursor = None
		while True:
			q = MovieQueryCache.all(keys_only=True)
			if cursor:
			   q.with_cursor(cursor)
			result = q.fetch(200)
			if not result:
			   break
			cursor = q.cursor()
			db.delete(result)


	except Exception, e:
		print 'error'
		pass