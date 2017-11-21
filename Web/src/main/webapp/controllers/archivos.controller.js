angular.module('ti').controller('ArchivosController',
		[ '$scope', '$rootScope', 'archivosService', ArchivosController ]);

function ArchivosController($scope, $rootScope, archivosService) {
	$rootScope.titulo = "Archivos";
	$rootScope.option="archivos";
	$scope.data=[];
	$scope.loadFiles=function() {
		 archivosService.list().then(
				 function(res){
					 $scope.data=res.data;
				 },
				 function(e){$scope.data=[];});
	}
	$scope.remove = function(id) {
		if(confirm("Desea eliminar el archivo seleccionado?")) {
			archivosService.remove(id).then(function(r){
				$scope.data.forEach(function(o,i){
					if(o.id==id) {
						$scope.data.splice(i,1);
						return false;
					}
				});
			})
		}
	};
	
	$scope.loadFiles();
	$scope.onFileSelect = function($files) {
		 $scope.mensajeSubida="";
		archivosService.upload($files).then(function(resp) {
        if (resp.status == 201) {
        	console.log(resp);
            $scope.mensajeSubida = "100% - location=" + resp.headers('location');
            $scope.loadFiles();
        }

    }, function(resp) {
        if (resp.status == 401) {
            $scope.mensajeSubida = "No autorizado.";
        } else {
            $scope.mensajeSubida = "Error subiendo";
        }
    }, function(evt) {
        var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
        $scope.mensajeSubida = progressPercentage + '% ';
    });

	};
}
