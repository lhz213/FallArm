<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="en_Us" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="../../favicon.ico">

<title>Home Page</title>

<!-- Bootstrap core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="css/dashboard.css" rel="stylesheet">

<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
<script src="js/ie-emulation-modes-warning.js"></script>
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">Fall Arm</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><c:if test="${nurse==null}">
							<a href="login.html">Register/Login</a>
						</c:if></li>
					<c:if test="${nurse!=null}">
						<li>
							<p class="navbar-text">
								Hello,
								<c:if test="${nurse.gender == 1 }">Mr. </c:if>
								<c:if test="${nurse.gender == 0 }">Mrs. </c:if>
								${nurse.lastName}
							</p>
						</li>
						<li><a
							href="/FallArm/PersonServlet?role=nurse&operation=search&id=${nurse.id}">Profile</a></li>
						<li><a href="/FallArm/LogoutServlet">Logout</a></li>
					</c:if>
					<li><a href="#">Help</a></li>
				</ul>
				<c:if test="${nurse!=null}">
					<form class="navbar-form navbar-right" action="SearchServlet"
						method="get">
						<div class="input-group">
							<input type="text" name="keyword" class="form-control"
								placeholder="Search..."><span class="input-group-btn">
								<button type="submit" class="btn btn-default">GO</button>
							</span>
						</div>
					</form>
				</c:if>
			</div>
		</div>
	</nav>

	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3 col-md-2 sidebar">
				<ul class="nav nav-sidebar">
					<li class="active"><a href="#">Overview <span
							class="sr-only">(current)</span></a></li>
					<li><a href="#">Reports</a></li>
					<li><a href="#">Analytics</a></li>
					<li><a href="#">Export</a></li>
				</ul>
			</div>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h1 class="page-header">Dashboard</h1>

				<div class="row placeholders">
					<div class="col-xs-6 col-sm-3 placeholder">
						<p class="img-responsive">
							<span class="badge badge-info">${totalNumberOfPatient}</span>
						</p>
						<h4>Total</h4>
						<span class="text-muted">The total number of patients</span>
					</div>
					<div class="col-xs-6 col-sm-3 placeholder">
						<p class="img-responsive">
							<span class="badge badge-info">${relatedNumberOfPatient}</span>
						</p>
						<h4>Related</h4>
						<span class="text-muted">The number of related patients</span>
					</div>
					<div class="col-xs-6 col-sm-3 placeholder">
						<p class="img-responsive">
							<span class="badge badge-danger">${totalNumberOfFall}</span>
						</p>
						<h4>Fall</h4>
						<span class="text-muted">The number of related patients
							fall</span>
					</div>
					<div class="col-xs-6 col-sm-3 placeholder">
						<p class="img-responsive">
							<span class="badge badge-warning">${totalNumberOfMaybe}</span>
						</p>
						<h4>Fall</h4>
						<span class="text-muted">The number of related patients
							maybe</span>
					</div>
				</div>
				<c:if test="${flag==true}">
					<div class="alert alert-success alert-dismissible" role="alert">
						<button type="button" class="close" data-dismiss="alert">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<strong>Succeed!</strong> ${info} Success!
					</div>
				</c:if>
				<c:if test="${flag==false}">
					<div class="alert alert-danger alert-dismissible" role="alert">
						<button type="button" class="close" data-dismiss="alert">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<strong>Failed!</strong> ${info} Failed!
					</div>
				</c:if>
				<c:if test="${recordList!=null}">
					<h2 class="sub-header">
						Record List
						<c:if test="${person!=null}">&nbsp;of&nbsp;${person.firstName}&nbsp;${person.lastName}</c:if>
					</h2>
					<div class="table-responsive">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>#</th>
									<th>Time</th>
									<th>Parameters</th>
									<th>Address</th>
									<th>State</th>
									<th>Option</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${recordList}" var="record" varStatus="status">
									<tr>
										<td>${status.count}</td>
										<td>${record.datetime}</td>
										<td>${record.data[0]}:${record.data[1]}:${record.data[2]}|${record.data[3]}:${record.data[4]}:${record.data[5]}</td>
										<td>${patient.honeAddress}</td>
										<td><c:if test="${record.state==1}">
												<span class="label label-danger">FALL</span>
											</c:if> <c:if test="${record.state==2}">
												<span class="label label-warning">MAYBE</span>
											</c:if> <c:if test="${record.state==3}">
												<span class="label label-success">FALL</span>
											</c:if></td>
										<td><a
											href="/FallArm/PersonServlet?role=contact&operation=edit&id=${contact.id}"><span
												class="label label-default">More Info</span></a>|<a
											href="/FallArm/PersonServlet?role=contact&operation=delete&id=${contact.id}"><span
												class="label label-default">Delete</span></a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:if>
				<c:if test="${patientList!=null}">
					<h2 class="sub-header">
						Patient List
						<c:if test="${person!=null}">&nbsp;of&nbsp;${person.firstName}&nbsp;${person.lastName}</c:if>
					</h2>
					<div class="table-responsive">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>#</th>
									<th>Patient ID</th>
									<th>Name</th>
									<th>Gender</th>
									<th>Email</th>
									<th>Related Nurse ID</th>
									<th>Option</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${patientList}" var="patient"
									varStatus="status">
									<tr>
										<td>${status.count}</td>
										<td><a
											href="/FallArm/PersonServlet?role=patient&operation=search&id=${patient.id}">${patient.id}</a></td>
										<td>${patient.firstName}&nbsp;${patient.lastName}</td>
										<td><c:if test="${patient.gender == 1}">Male</c:if> <c:if
												test="${patient.gender == 0}">Female</c:if></td>
										<td><a href="mailto:${patient.emailAddress}">${patient.emailAddress}</a></td>
										<td>${patient.relatedNurseId}</td>
										<td><a
											href="/FallArm/PersonServlet?role=patient&operation=edit&id=${patient.id}"><span
												class="label label-default">Edit</span></a>|<a
											href="/FallArm/PersonServlet?role=patient&operation=delete&id=${patient.id}"><span
												class="label label-default">Delete</span></a>|<a
											href="/FallArm/SearchServlet?patientId=${patient.id}&role=contact"><span
												class="label label-default">Contact</span></a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:if>
				<a href="/FallArm/PersonServlet?role=patient&operation=add"><span
					class="label label-primary">ADD PATIENT</span></a>
				<c:if test="${contactList!=null}">
					<h2 class="sub-header">
						Emergency Contact List
						<c:if test="${person!=null}">&nbsp;of&nbsp;${person.firstName}&nbsp;${person.lastName}</c:if>
					</h2>
					<div class="table-responsive">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>#</th>
									<th>Name</th>
									<th>Gender</th>
									<th>Email</th>
									<th>Phone Number</th>
									<th>Option</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${contactList}" var="contact"
									varStatus="status">
									<tr>
										<td>${status.count}</td>
										<td><a
											href="/FallArm/PersonServlet?role=contact&operation=search&id=${contact.id}">${contact.firstName}&nbsp;${contact.lastName}</a></td>
										<td><c:if test="${contact.gender == 1}">Male</c:if> <c:if
												test="${contact.gender == 0}">Female</c:if></td>
										<td><a href="mailto:${contact.emailAddress}">${contact.emailAddress}</a></td>
										<td>${contact.phoneNumber}</td>
										<td><a
											href="/FallArm/PersonServlet?role=contact&operation=edit&id=${contact.id}"><span
												class="label label-default">Eidt</span></a>|<a
											href="/FallArm/PersonServlet?role=contact&operation=delete&id=${contact.id}"><span
												class="label label-default">Delete</span></a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:if>
				<a href="/FallArm/PersonServlet?role=contact&operation=add"><span
					class="label label-primary">ADD CONTACT</span></a>
			</div>
		</div>
	</div>

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/docs.min.js"></script>
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>
