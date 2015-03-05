
var dataStored = document.querySelectorAll("input");
var request = {
	"method": "POST",
	"url": "http://localhost:9998/product/create",
	"data": {
		"id_product": null ,
		"na_product": null,
		"pp_product": null,
		"sp_product": null,
		"qt_product": null
	},
	"headers": {
		"Content-Type": "application/json"
	}
};
angular.module('angularApp', [])
	.controller('InventoryController', ['$scope', '$http',
		function ($scope, $http) {
			$scope.sendProduct = function () {
				request.data.id_product = parseInt(document.getElementById('id').value);
				request.data.na_product = document.getElementById('name').value;
				request.data.pp_product = parseFloat(document.getElementById('purchasePrice').value);
				request.data.sp_product = parseFloat(document.getElementById('salePrice').value);
				request.data.qt_product = parseInt(document.getElementById('quantity').value);
				console.log(request.data);
				$http(request).success(function (data) {
					console.log(data);
				}).error(function (error) {
					console.log(error);
				});
			};
		}]);

function turnfloat(n){
	if(n.indexOf("."==-1)){
		return n +".00";
	}
	else return n;
}