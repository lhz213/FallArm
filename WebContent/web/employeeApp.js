
var app = angular.module('employeeApp', []);



app.controller('customersController', function($scope,$http) {
    $http.get('dummy_data.json')
    .success(function(response) 
	{
		$scope.employees = response.employees;
	});
	
	
	$scope.addUser = function()
	{
	  var newEmp = {
	     "name": $scope.newName,
		 "class": $scope.newSkill,
		 "address":$scope.newAddress,
		 "gender":$scope.newGender,
		 "phone":$scope.newPhone,
		 "email":$scope.newEmail,
		 "bd":$scope.newBD,
		 "condition":$scope.newCondition ="good"
	  };
	  
	  if(($scope.newName !=null)&&($scope.newSkill !=null)&&($scope.newAddress !=null)&&($scope.newGender !=null)&&($scope.newPhone != null))
	  {
			$scope.employees.push(newEmp);
		
	  }
	}
	
	$scope.editUser = function(name)
	{
	 for(i in $scope.employees) {
            if($scope.employees[i].name == name) {
            $scope.newName = $scope.employees[i].name;
			 $scope.newSkill = $scope.employees[i].class;
			 $scope.newAddress = $scope.employees[i].address;
			 $scope.newGender = $scope.employees[i].gender;
			 $scope.newEmail = $scope.employees[i].email;	
			 $scope.newPhone = $scope.employees[i].phone;
			 $scope.newBD = $scope.employees[i].bd;

			 $scope.temp_id = $scope.employees[i].id;
            }
        }  
	
	}
	
	$scope.deleteUser = function(name)
	{
		for(i in $scope.employees)
		{
			//alert("you really wanna delete " +$scope.employees[i].name +" ??");
			if($scope.employees[i].name ==name)
			{
				/* var index = $scope.employees.indexOf(i); */
				if(i > -1){
				$scope.employees.splice(i,1);
				}
			}
		}
		
	}
	
	$scope.checkUser = function(name)
	{
		for(i in $scope.employees)
		{

			if($scope.employees[i].name == name)
			{
				$scope.newName = $scope.employees[i].name;
			 $scope.newSkill = $scope.employees[i].class;
			$scope.newCondition	= $scope.employees[i].condition;
			$scope.newGender = $scope.employees[i].gender;
			$scope.newEmail = $scope.employees[i].email;	
			$scope.newPhone = $scope.employees[i].phone;
			 $scope.newBD = $scope.employees[i].bd;

			 $scope.temp_id = $scope.employees[i].id;
				
			}

		}


	}
	$scope.showAlert = function() 
	{ 
		   var myText = "PATIENT FALLS!!! NEED HELP!!!";
		
		   alert (myText +"  "+ "Patient Name: " +$scope.employees[1].name+" Patient ID: "+$scope.employees[1].class
		+" Patient Address: "+$scope.employees[1].address);
				  
	}

	$scope.saveEditChanges = function()
	{
       for(i in $scope.employees) {
            if($scope.employees[i].id ==  $scope.temp_id) {
               $scope.employees[i].name =  $scope.newName;
		       $scope.employees[i].class = 	 $scope.newSkill;
		       $scope.employees[i].gender = $scope.newGender;
		       $scope.employees[i].address =$scope.newAddress;
		       $scope.employees[i].email = $scope.newEmail;
			   $scope.employees[i].phone = $scope.newPhone;
			   $scope.employees[i].bd = $scope.newBD ;

            }
        }  		
	}
	
});


	  

sampleApp .config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/addOrder', {
        templateUrl: 'templates/add-order.html',
        controller: 'AddOrderController'
      }).
      when('/showOrders', {
        templateUrl: 'templates/show-orders.html',
        controller: 'ShowOrdersController'
      }).
      otherwise({
        redirectTo: '/addOrder'
      });
  }]);



/*
function customersController($scope,$http) {
    $http.get('dummy_data.json')
    .success(function(response) {$scope.employees = response.employees;});
}
*/
