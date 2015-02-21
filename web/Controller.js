/*var a = {
	id_client: 4,
	na_client: "Iker",
	la_client: "Asner",
	ci_client: 12345655
};

var b = (JSON.stringify(a));
*/
var req = {
	method: 'GET',
	url: 'http://localhost:9998/product/all',
	headers: {
		'Content-Type': "application/json"
	}
};
var productget;
var myapp=angular.module('App', []);

	myapp.controller('myController', function ($scope, $http) {
		$scope.send = $http(req).success(function (response) {
			console.log(response);
			$scope.products = response.products;
		}).error(function (response) {
			console.log(response);
		});
	});

