angular.module('ti')
.factory('archivosService',['$http','URL_API_BASE', 'Upload',function($http, URL_API_BASE, Upload) {
   return {
	   upload: function(files) {
	  	 return Upload.upload({
	        url: URL_API_BASE+'archivos',
	        file: files
	    });
	   },
	   list: function() {
			   return $http.get(URL_API_BASE+"archivos");
	   },
	   remove: function(id) {
		   return $http.delete(URL_API_BASE+"archivos/"+id);
	   } 
   }
}]);
