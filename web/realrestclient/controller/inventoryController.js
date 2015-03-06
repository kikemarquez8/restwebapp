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
				var id = parseInt(document.getElementById('saleid').value);
				$http.get("http://localhost:9998/sale/"+ id+"/products").
					success(function(data){
						console.log(data);
						processSale(data);
					}).error(function(data){
						alert(data.message);
					})
			};

			var processSale = function(data){
				var captions = document.getElementById('namesale');
				var table = document.getElementsByClassName('table')[0];
				if(data.info_sale != null){
					captions.innerHTML = "Name: "+data.info_sale.na_client + " </br> Sale N: "+data.info_sale.id_sale;
					for(var i= 0; i< data.products.length;i++) {
						table.insertRow(table.rows.length);
						table.rows[table.rows.length - 1].insertCell(0).innerHTML = data.products[i].id_product;
						table.rows[table.rows.length - 1].insertCell(1).innerHTML = data.products[i].na_product;
						table.rows[table.rows.length - 1].insertCell(2).innerHTML = data.products[i].qt_product;
					}
				}
			};
			$scope.getProducts = function () {
				$http.get("http://localhost:9998/product/all").
					success(function(data){
						console.log(data);
						processProducts(data);
					}).error(function(data){
						alert(data.message);
					})
			}

			var processProducts = function(data){
				var table = document.getElementsByClassName('table')[0];
				console.log()
				if(data.products != null){
					for(var i= 0; i< data.products.length;i++) {
						table.insertRow(table.rows.length);
						table.rows[table.rows.length - 1].insertCell(0).innerHTML = data.products[i].id_product;
						table.rows[table.rows.length - 1].insertCell(1).innerHTML = data.products[i].na_product;
						table.rows[table.rows.length - 1].insertCell(2).innerHTML = data.products[i].sp_product;
						table.rows[table.rows.length - 1].insertCell(3).innerHTML = data.products[i].qt_product;
					}
				}
			}
		}]);
