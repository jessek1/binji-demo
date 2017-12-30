# binji-demo

## binji-data-import
Tool used to import data from gracenote xml and ces app google sheet.
Requires direct environment access, i.e. db connections etc

## gracenote images
See [OvdImageService](https://github.com/jessek1/binji-demo/blob/master/binji-demo-services/src/main/java/binji/demo/services/ovd/OvdImageServiceImpl.java) for fallback logic in selecting images to use for content.
Needs more fallbacks added to cover the majority of the content.

Currenty am using 4x3 aspect ratio images.

### resizing gracenote images
Images can be resized by providing a w or h request param.
{image_url}&w=200

The aspect ratio of the image will always be kept.  Width has precedence over height if both are supplied.

## slow intial loads
No eager fetching of data has been enabled yet so initial loads are slow.  I will add this soon.  Subsequent loads should be fast as after the initial load the result will be cached. 

## api
temp base url: http://dev.d360technologies.com/binji-demo-webapi/api/v0.0.0

•	Content
o	/ovdcontent/provider/{name}
	The name is like query so it will try and pick up anything that matches.
	There were some international providers that I’ve excluded via hardcoding in the query, you can see lists attached.
	Params
•	genres
o	single or csv
	Example
•	/ovdcontent/provider/netflix?genres=action,war&p=0&s=20
o	/ovdcontent/genre/{genre}
	Single or csv
	Example
•	/ovdcontent/genre/action?p=0&s=2
o	/ovdcontent/{tmsId}
	Singular item retrieval
•	Apps
o	/apps/{genre}
	Example
•	/apps/genre/movie
	This is just an import of the google sheet so once the sheet is updated, can rerun the import to update the db here.
o	/apps/{id}

## providers catalog data being used
'Adult Swim'
'Amazon.com'
'CBS'
'Crackle'
'Disney'
'HBO'
'Hulu'
'iTunes Store'
'MTV'
'National Geographic'
'NBC'
'Netflix'
'nick.com'
'PBS'
'TBS'
'CW'
'tv.com'
'YouTube'
'ABC'
'Freeform'
'A&E'
'AMC'
'BBC America'
'BET'
'Cartoon Network'
'Food Network'
'Fox'
'Lifetime'
'Showtime'
'SnagFilms'
'SyFy'
'TNT'
'TV Land'
'USA Network'
'VH1'
'WE tv'
'Spike'
'Comedy Central'
'Cinemax'
'VUDU'
'FilmFlex'
'BBC'
'GlobalTV'
'ShowCase'
'Channel 4'
'Channel 5'
'Verizon Flexview'
'Netflix SE'
'Epix'
'Bravo'
'Eonline'
'History Channel'
'FYI'
'Docplay'
'Fox Sports'

## providers catalog data being excluded
'XFINITY', 
'Netflix MX', 
'YouTube MX',
'Netflix DE',
'Netflix UK',
'YouTube CA',
'YouTube UK',
'Netflix CA',
'iTunes CA',
'iTunes UK',
'ITV',
'CBC',
'CTV',
'YouTube MX',
'Netflix DE',
'Netflix FR',
'Netflix DK',
'Netflix FI',
'Netflix NO',
'ZDF',
'Arte',
'Maxdome',
'TF1',
'FilmoTV',
'France TV Pluzz VAD',
'France TV Pluzz',
'Wuaki ES',
'6Play',
'Chili Web',
'ARD',
'Amazon UK',
'iTunes DE',
'iTunes FR',
'Videociety DE',
'Netflix AT',
'Viewster DE',
'Viewster AT',
'Crackle CA',
'Netflix CH-fr',
'Netflix CH-de',
'Netflix BR',
'TNT Go BR',
'Space Go BR',
'Telecine Play BR',
'Crackle BR',
'iTunes BR',
'YouTube BR',
'Cartoon Go BR',
'Esporte Interativo',
'Globosat GNT',
'Globosat Universal',
'Globosat SporTV',
'Globosat Multishow',
'Globosat BIS',
'Globosat Viva',
'Globosat Canal Off',
'Globosat Gloob',
'Globosat GloboNews',
'Globosat Canal Brasil',
'Globosat Megapix',
'Globosat Combate',
'Globosat Globosat',
'Espn BR',
'Philos',
'7TV',
'Sexy Hot',
'Sky',
'Netflix AU',
'Fox Brazil Fox',
'Fox Brazil FX',
'Fox Brazil Sports',
'Fox Brazil Life',
'Fox Brazil National Geographic',
'Netflix NZ',
'iTunes AU',
'Telstra',
'SBS',
'Stan',
'Tenplay',
'Wuaki FR',
'Wuaki DE',
'Wuaki IT',
'Space Go MX',
'TNT Go MX',
'Fox Mexico Fox',
'Fox Mexico Sports',
'Fox Mexico FX',
'Fox Mexico Life',
'Fox Mexico National Geographic',
'Fox Mexico Plus',
'Wuaki UK',
'PlusSeven',
'NineNow',
'ABCiView',
'Globosat Syfy',
'Fox Brazil Premium',
'UKTV',
'RTL TV Now',
'HBO BR Documentaries',
'HBO BR Kids',
'HBO BR Movies',
'HBO BR Series',
'HBO BR Specials',
'HBO BR Adult',
'CW Seed',
'Discovery Kids BR',
'Foxtel Play',
'Noggin BR',
'AXN BR',
'A&E BR',
'E! BR',
'Telemundo',
'Sony BR',
'History BR',
'Lifetime BR',
'Telekom Sport',
'Netflix AU Telstra',
'Fox Mexico Premium',
'Hayu UK',
'VM Store UK',
'SevenPlus'
