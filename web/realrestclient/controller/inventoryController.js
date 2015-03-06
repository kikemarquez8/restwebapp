var dataStored = document.querySelectorAll("input");
var request = {
	"method": "POST",
	"url": "http://localhost:9998/product/create",
	"data": {
		"id_product": "",
		"na_product": "",
		"pp_product": "",
		"sp_product": "",
		"qt_product": ""
	},
	"headers": {
		"Content-Type": "application/json"
	}
};
angular.module('angularApp', [])
	.controller('InventoryController', ['$scope', '$http',
		function ($scope, $http) {
			$scope.sendProduct = function () {
				request.data.id_product = document.getElementById('id').value + "";
				request.data.na_product = document.getElementById('name').value + "";
				request.data.pp_product = document.getElementById('purchasePrice').value + "";
				request.data.sp_product = document.getElementById('salePrice').value + "";
				request.data.qt_product = document.getElementById('quantity').value + "";
				console.log(request.data);
				$http(request).success(function (data) {
					console.log(data);
				}).error(function (error) {
					console.log(error);
				});
			};
		}]);
