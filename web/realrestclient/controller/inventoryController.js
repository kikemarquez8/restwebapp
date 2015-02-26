
var dataStored = document.querySelectorAll("input");
var request = {
	"method": "POST",
	"url": "http://localhost:9998/product/create",
	"data": {
		"id_product": 7 ,//parseInt(dataStored[0].value),
		"na_product": "Macbookpro", //dataStored[1].value,
		"pp_product": 400.04, //parseFloat(dataStored[2].value),
		"sp_product": 600.04, //parseFloat(dataStored[3].value),
		"qt_product": 90//parseInt(dataStored[4].value)
	},
	"headers": {
		"Content-Type": "application/json"
	}
};
angular.module('angularApp', [])
	.controller('InventoryController', ['$scope', '$http',
		function ($scope, $http) {
			$scope.sendProduct = function () {
				$http(request).success(function (data) {
					window.alert(data);
				}).error(function (error) {
					window.alert(error);
				});
			};
		}]);