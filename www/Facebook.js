var facebook = {
    login: function(successCallback, errorCallback) {
		cordova.exec(successCallback,
		            errorCallback,
		            "Facebook_Plugin",
		            "login",
		            [{"file_path": "","message_text":""}]
                );
    	},
   	logout: function(successCallback, errorCallback) {
				cordova.exec( successCallback,
				            errorCallback,
							"Facebook_Plugin",
							"logout",
		        			[{"file_path": "","message_text":""}]
		        		);
	    },
	share: function(successCallback, errorCallback, file_path, message_text) {
				cordova.exec( successCallback,
				            errorCallback,
							"Facebook_Plugin",
							"share",
							[{"file_path": file_path,"message_text":message_text}]
		        		);
    },
    check_loggedin: function(successCallback, errorCallback) {
					cordova.exec( successCallback,
					            errorCallback,
								"Facebook_Plugin",
								"check_logged_in",
								[{"file_path": "","message_text":""}]
			        		);
    },
    messenger: function(successCallback, errorCallback, file_path) {
						cordova.exec( successCallback,
						            errorCallback,
									"Facebook_Plugin",
									"messenger",
									[{"file_path": file_path,"message_text":""}]
				        		);
    }
}

module.exports = facebook;

