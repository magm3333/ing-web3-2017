angular.module('ti').config(
		function($routeProvider, $locationProvider, $httpProvider) {
			$httpProvider.defaults.withCredentials = true;

			$httpProvider.interceptors.push('APIInterceptor');
			
			$locationProvider.hashPrefix('!');
			$routeProvider.when('/', {
				templateUrl : 'views/main.html',
				controller : 'MainController'
			}).when('/entidades', {
				templateUrl : 'views/entidades.html',
				controller : 'EntidadesController'
			}).when('/archivos', {
				templateUrl : 'views/archivos.html',
				controller : 'ArchivosController'
			}).otherwise({
				redirectTo : '/'
			});
		});
