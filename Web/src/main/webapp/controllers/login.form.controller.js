angular.module('ti')
.controller('LoginFormController', ['$rootScope', '$scope', '$log', '$uibModalInstance', 'coreService','user',  LoginFormController]);
function LoginFormController($rootScope, $scope, $log, $uibModalInstance, coreService, user) {
	$scope.title="Ingreso";
	$scope.user=user;
	$scope.login = function () {
		coreService.login(user).then(
			function(resp){ 
				if(resp.status===200) {
					$rootScope.loginOpen=false;
					$uibModalInstance.dismiss();
					coreService.pingAuth().then(
							function(resp){ 
								  if(resp.status===200 && resp.data.code==0) {
									  $rootScope.user.name=resp.data.username;
									  $rootScope.authenticated=true;
								  } else {
									  $rootScope.authenticated=false;
									  $rootScope.openLoginForm();
								  }
								  $rootScope.regularCall=true;
								});
				}
			},
			function(respErr){
				$log.log(respErr);
			}
		);
	  };
}
