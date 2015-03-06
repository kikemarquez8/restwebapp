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
				readForm();
				console.log(request.data);
				$http(request).success(function (data) {
					alert(data.message + ", the product was inserted");
					console.log(data);
				}).error(function (error) {
					alert(error.message + ", the product couldn't be inserted");
				});
			};

			$scope.testID = function () {
				var id = document.querySelector("#id");
				id.value = parseInt(id.value);
				document.getElementById('id').style.backgroundColor = "white";

				if (id.value !== null && id.value !== "") {
					$http.get("http://localhost:9998/product/" + id.value).
						success(function (data) {

							//if the product already exists.
							if (data.message === undefined) {
								id.style.backgroundColor = "#d14";
							} else {
								id.style.backgroundColor = "steelblue";
								id.style.color = "white";
							}
						}).error(function (data) {
							alert(data.message);
						});
				}
			};

			$scope.clearField = function () {
				document.getElementById('id').style.backgroundColor = "white";
				document.getElementById('id').value = "";
				document.getElementById('name').value = "";
				document.getElementById('purchasePrice').value = "";
				document.getElementById('salePrice').value = "";
				document.getElementById('quantity').value = "";
			};

			var readForm = function () {
				request.data.id_product = document.getElementById('id').value + "";
				request.data.na_product = document.getElementById('name').value + "";
				request.data.pp_product = document.getElementById('purchasePrice').value + "";
				request.data.sp_product = document.getElementById('salePrice').value + "";
				request.data.qt_product = document.getElementById('quantity').value + "";
			};
			$scope.getSales = function(){
				var id = parseInt(document.getElementById('saleid'));
				$http.get("http://localhost:9998/product/"+ id).
					succes(function(data){

					}).error(function(data){
						alert(data.message);
					})
			}
		}]);
