var test =angular.module('employeeApp', ['ui.bootstrap']);
test.controller('customersController', function ($scope) {
  $scope.dynamicPopover = 'Hello, World!';
  $scope.dynamicPopoverTitle = 'Title';
});