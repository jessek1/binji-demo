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
