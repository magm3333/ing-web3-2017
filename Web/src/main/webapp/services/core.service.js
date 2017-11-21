angular.module('ti')
.factory('coreService',['$http','URL_API_BASE','URL_BASE',function($http, URL_API_BASE,URL_BASE) {
   return {
	   pingAuth: function() {
		   return $http.get(URL_API_BASE+"core/pingAuth");
	   },
	   login: function(user) {
		   var req = {
				method: 'POST',
				url: URL_BASE+'login',
				headers : { 'Content-Type': 'application/x-www-form-urlencoded' },
				data: 'username='+user.name+'&password='+user.password
		   };
		   return $http(req);
	   },
	   logout: function() {
		   return $http.get(URL_BASE+"logout");
	   }
	   
   }
}]);
