angular.module('ti').controller('ArchivosController',
		[ '$scope', '$rootScope', ArchivosController ]);

function ArchivosController($scope, $rootScope) {
	$rootScope.titulo = "Archivos";
	$rootScope.option="archivos";
	
}
