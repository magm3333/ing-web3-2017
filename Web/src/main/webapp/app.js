angular.module(
		'ti',
		[ 'ngRoute', 'ngSanitize', 'ui.select', 'ui.bootstrap', 'ngTagsInput',
				'ngLoadingSpinner', 'angularUtils.directives.dirPagination',
				'angularInlineEdit', 'adaptv.adaptStrap', 'ngFileUpload' ])
		.constant('URL_BASE', '/ti/').constant('URL_API_BASE', '/ti/api/v1/')
		.factory('focus', function($timeout, $window) {
			return function(id) {
				$timeout(function() {
					var element = $window.document.getElementById(id);
					if (element)
						element.focus();
				});
			};
		}).filter(
				'highlight',
				function($sce) {
					return function(text, phrase) {
						if (phrase && text) {
							text += '';
							text = text.replace(new RegExp('(' + phrase + ')', 'gi'),
									'<span style="background: yellow">$1</span>');
						}

						return $sce.trustAsHtml(text);
					}
				}).filter('limitString', function() {
			return function(string, limit) {
				if (string && string.length > limit && string.length > limit + 3)
					string = string.substring(0, limit - 3) + '...';
				return string;
			}
		}).filter('filesize', function () {
			return function (size) {
				if (isNaN(size))
					size = 0;

				if (size < 1024)
					return size + ' Bytes';

				size /= 1024;

				if (size < 1024)
					return size.toFixed(2) + ' Kb';

				size /= 1024;

				if (size < 1024)
					return size.toFixed(2) + ' Mb';

				size /= 1024;

				if (size < 1024)
					return size.toFixed(2) + ' Gb';

				size /= 1024;

				return size.toFixed(2) + ' Tb';
			};
		}).run(
				[
						'$rootScope',
						'$location',
						'$interval',
						'$log',
						'$uibModal',
						'$http',
						'coreService',
						function($rootScope, $location, $interval, $log, $uibModal, $http,
								coreService) {
							$rootScope.relocate = function(loc) {
								setTimeout(function() { $location.path(loc); },0);
								//$location.path(loc);
							}

							$rootScope.regularCall = true;
							$rootScope.authenticated = false;
							$rootScope.user = {
								name : "",
								password : ""
							};
							$rootScope.loginOpen = false;
							$rootScope.meses = [ 'Enero', 'Febrero', 'Marzo', 'Abril',
									'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre',
									'Noviembre', 'Diciembre' ];

							$rootScope.openLoginForm = function(size) {
								if (!$rootScope.loginOpen) {
									$rootScope.loginOpen = true;
									$uibModal.open({
										animation : true,
										backdrop : 'static',
										keyboard : false,
										templateUrl : 'views/loginForm.html',
										controller : 'LoginFormController',
										size : size,
										resolve : {
											user : function() {
												return $rootScope.user
											}
										}
									});
								}
							};
							$rootScope.logout=function() {
								console.log("logout");
								coreService.logout().then(function(){
									$rootScope.authenticated = false;
									coreService.pingAuth();
								});
							}
							$rootScope.recoverUserInfo = function() {
								coreService.pingAuth().then(function(resp) {
									if (resp.status === 200 && resp.data.code == 0) {
										$rootScope.user.name = resp.data.username;
										$rootScope.authenticated = true;
									} else {
										$rootScope.authenticated = false;
										$rootScope.openLoginForm();
									}
									$rootScope.regularCall = true;
								});
							}

							$interval(function() {
								if (!$rootScope.user.name) {
									$rootScope.recoverUserInfo();
								}
							}, 200);

						} ]);