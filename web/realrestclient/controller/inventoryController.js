angular.module('angularApp', [])
	.controller('InventoryController', ['$scope', '$http',
		function ($scope, $http) {
			var dataStored = document.querySelectorAll("input");
			var request = {
				"method": "POST",
				"url": "http://localhost:9998/product/create",
				"data": {
					"id_product": parseInt(dataStored[0].value),
					"na_product": dataStored[1].value + "",
					"pp_product": parseFloat(dataStored[2].value),
					"sp_product": parseFloat(dataStored[3].value),
					"qt_product": parseInt(dataStored[4].value)
				},
				"headers": {
					"Content-Type": "application/json"
				}
			};

			$scope.sendProduct = function () {
				dataStored = document.querySelectorAll("input");
				request.data.id_product = parseInt(dataStored[0].value);
				request.data.na_product = dataStored[1].value + "";
				request.data.pp_product = parseFloat(dataStored[2].value);
				request.data.sp_product = parseFloat(dataStored[3].value);
				request.data.qt_product = parseInt(dataStored[4].value);

				$http(request).success(function (data) {
					window.alert(data);
				}).error(function (error) {
					window.alert(error);
				});
			};
		}]);