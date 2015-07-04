*************Cordova : Facebook SDK 4.0*****************


By using this plugin you can login, logout, check loggedin user, share and also share images on messenger



------------------------------------------------------------------------------------------

First you need to download the facebook plugin, So go here to download Facebook SDK 4.3.0 

https://developers.facebook.com/resources/facebook-android-sdk-4.3.0.zip

-----------------------------------------------------------------------------------------



Install this plugin using:

cordova plugin add com.jp.plugin.facebook



Remove Plugins :

cordova plugin remove com.jp.plugin.facebook


----------------------------------------------------------------------------------------------

After installed plugin you have to declare following activity in android mainest file:

<activity android:name="com.jp.plugin.facebook.Facebook_Functions"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

<activity android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
            android:label="@string/app_name" android:name="com.facebook.FacebookActivity"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />


----------------------------------------------------------------------------------------------

After that add metadata in your manifest file

<meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id" />


----------------------------------------------------------------------------------------------


Put the below code in your javascript code: 


For login:


facebook.login(
	function(success)
	{
            console.log(success);
        }, 
	function(error)
	{
            console.log(error);
        }
    );


------------------------------------------------------------------------------------------------


For logout:


facebook.logout(
	function(success)
	{
            console.log(success);
        }, 
	function(error)
	{
            console.log(error);
        }
    );


------------------------------------------------------------------------------------------------


For check user loggedin:


facebook.check_loggedin(
	function(success)
	{
            console.log(success);
        }, 
	function(error)
	{
            console.log(error);
        }
    );


------------------------------------------------------------------------------------------------


For share post:

facebook.share(
	function(success)
	{
            console.log(success);
        }, 
	function(error)
	{
            console.log(error);
        },
	file_path, message_text
    );

file_path: image or video path you want to share
message_text: String message you want to share


------------------------------------------------------------------------------------------------


For share images on facebook messenger:



facebook.messenger(
	function(success)
	{
            console.log(success);
        }, 
	function(error)
	{
            console.log(error);
        },
	file_path
    );

file_path: image path you want to share



------------------------------------------------------------------------------------------------



GitHub URL:   https://github.com/jaypratapsingh/Facebook

npm url :     https://www.npmjs.com/package/com.jp.plugin.facebook