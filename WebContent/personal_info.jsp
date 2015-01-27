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
						<input type="text" name="keyword" class="form-control"
							placeholder="Search...">
						<button type="submit" class="btn">GO</button>
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
				<c:if test="${role=='patient'&&operation=='search'&&patient!=null}">
					<h1 class="page-header">Profile of ${role}: ${patient.id}</h1>
					<div class="row-fluid">
						<div class="col-md-11 col-md-offset-1">
							<div class="row">
								<div class="col-md-11 col-md-offset-1"></div>
							</div>
							<div class="row-fluid">
								<div class="col-sm-3">
									<img src="img/touxiang.png" class="img-rounded">
								</div>
								<div class="row">
									<div class="col-sm-3">
										<h4>First Name: ${patient.firstName}</h4>
										<br>
										<h4>Last Name: ${patient.lastName}</h4>
										<br>
										<h4>
											Gender:
											<c:if test="${patient.gender==1}">Male</c:if>
											<c:if test="${patient.gender==0}">Female</c:if>
										</h4>
										<br>
									</div>
									<div class="col-sm-3">
										<h4>Age: ${patient.age}</h4>
										<br>
										<h4>Birth Date: ${patient.birthdateStr}</h4>
										<br>
										<h4>
											<a href="mailto:${patient.emailAddress}">${patient.emailAddress}</a>
										</h4>
										<br>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6 col-sm-offset-3">
										<h4>Home Address: ${patient.homeAddress}</h4>
										<br>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-3 col-sm-offset-2">
										<h4>
											<a
												href="/FallArm/PersonServlet?role=patient&operation=edit&id=${patient.id}"><span
												class="label label-info">EDIT</span></a>
										</h4>
									</div>
									<div class="col-sm-2">
										<h4>
											<a
												href="/FallArm/PersonServlet?role=patient&operation=delete&id=${patient.id}"><span
												class="label label-danger">DELETE</span></a>
										</h4>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:if>
				<c:if test="${role=='nurse'&&operation=='search'&&nurse!=null}">
					<h1 class="page-header">Profile of ${role}: ${nurse.id}</h1>
					<div class="row-fluid">
						<div class="col-md-11 col-md-offset-1">
							<div class="row">
								<div class="col-md-11 col-md-offset-1"></div>
							</div>
							<div class="row-fluid">
								<div class="col-sm-3">
									<img src="img/touxiang.png" class="img-rounded">
								</div>
								<div class="row">
									<div class="col-sm-3">
										<h4>First Name: ${nurse.firstName}</h4>
										<br>
										<h4>Last Name: ${nurse.lastName}</h4>
										<br>
										<h4>
											Gender:
											<c:if test="${nurse.gender==1}">Male</c:if>
											<c:if test="${nurse.gender==0}">Female</c:if>
										</h4>
										<br>
									</div>
									<div class="col-sm-3">
										<h4>Age: ${nurse.age}</h4>
										<br>
										<h4>Birth Date: ${nurse.birthdateStr}</h4>
										<br>
										<h4>
											<a href="mailto:${nurse.emailAddress}">${nurse.emailAddress}</a>
										</h4>
										<br>
									</div>
								</div>

								<div class="row">
									<div class="col-sm-3 col-sm-offset-2">
										<h4>
											<a
												href="/FallArm/PersonServlet?role=nurse&operation=edit&id=${nurse.id}"><span
												class="label label-info">EDIT</span></a>
										</h4>
									</div>
									<div class="col-sm-2">
										<h4>
											<a
												href="/FallArm/PersonServlet?role=nurse&operation=delete&id=${nurse.id}"><span
												class="label label-danger">DELETE</span></a>
										</h4>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:if>
				<c:if test="${role=='contact'&&operation=='search'&&contact!=null}">
					<h1 class="page-header">Profile of ${role}: ${contact.id}</h1>
					<div class="row-fluid">
						<div class="col-md-11 col-md-offset-1">
							<div class="row">
								<div class="col-md-11 col-md-offset-1"></div>
							</div>
							<div class="row-fluid">
								<div class="col-sm-3">
									<img src="img/touxiang.png" class="img-rounded">
								</div>
								<div class="row">
									<div class="col-sm-3">
										<h4>First Name: ${contact.firstName}</h4>
										<br>
										<h4>Last Name: ${contact.lastName}</h4>
										<br>
										<h4>
											Gender:
											<c:if test="${contact.gender==1}">Male</c:if>
											<c:if test="${contact.gender==0}">Female</c:if>
										</h4>
										<br>
									</div>
									<div class="col-sm-3">
										<h4>Patient ID: ${contact.patientId}</h4>
										<br>
										<h4>Phone Number: ${contact.phoneNumber}</h4>
										<br>
										<h4>
											<a href="mailto:${contact.emailAddress}">${contact.emailAddress}</a>
										</h4>
										<br>
									</div>
								</div>

								<div class="row">
									<div class="col-sm-3 col-sm-offset-2">
										<h4>
											<a
												href="/FallArm/PersonServlet?role=contact&operation=edit&id=${contact.id}"><span
												class="label label-info">EDIT</span></a>
										</h4>
									</div>
									<div class="col-sm-2">
										<h4>
											<a
												href="/FallArm/PersonServlet?role=contact&operation=delete&id=${contact.id}"><span
												class="label label-danger">DELETE</span></a>
										</h4>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:if>
				<c:if test="${role=='patient'&&operation=='add'}">
					<h1 class="page-header">Add New ${role}</h1>
					<div class="row-fluid">
						<div class="col-md-11 col-md-offset-1">
							<div class="row">
								<div class="col-md-11 col-md-offset-1"></div>
							</div>

							<div class="row-fluid">
								<div class="col-sm-3">
									<img src="img/touxiang.png" class="img-rounded">
								</div>
								<form action="PersonServlet" method="POST">
									<div class="row">
										<div class="col-sm-3">
											<div class="input-group">
												<span class="input-group-addon">*</span> <input type="text"
													name="first_name" class="form-control"
													placeholder="First Name" required="required">
											</div>
											<br>
											<div class="input-group">
												<span class="input-group-addon">*</span> <input type="text"
													name="last_name" class="form-control"
													placeholder="Last Name" required="required">
											</div>
											<br>
											<div class="col-sm-6 input-group">
												<span class="input-group-addon"> <input type="radio"
													name="gender" value="1" checked="checked">
												</span> <input type="text" readonly="readonly" class="form-control"
													value="Male">
											</div>
											<br>
											<div class="col-sm-6 input-group">
												<span class="input-group-addon"> <input type="radio"
													name="gender" value="0">
												</span><input type="text" readonly="readonly" class="form-control"
													value="Female">
											</div>
											<br>
										</div>
										<div class="col-sm-3">
											<div class="input-group">
												<span class="input-group-addon">*</span> <input
													type="number" min=500001 name="related_nurse_id"
													class="form-control" placeholder="Related Nurse ID">
											</div>
											<br>
											<div class="input-group">
												<span class="input-group-addon">*</span> <input type="date"
													name="birthdate" class="form-control" value="1980-01-01"
													placeholder="Birth Date: yyyy-mm-dd">
											</div>
											<br>
											<div class="input-group">
												<span class="input-group-addon">*</span> <input
													type="password" name="password" class="form-control"
													placeholder="Password" required="required">
											</div>
											<br>
											<div class="input-group">
												<span class="input-group-addon">*</span> <input
													type="password" name="re-password" class="form-control"
													placeholder="Re-type Password" required="required">
											</div>
											<br>
										</div>
									</div>
									<div class="col-sm-7 col-sm-offset-3">
										<div class="col-sm-8 input-group">
											<span class="input-group-addon">*</span> <input type="email"
												name="email_address" class="form-control"
												placeholder="Email Address" required="required">
										</div>
										<br>
									</div>
									<div class="col-sm-7 col-sm-offset-3">
										<div class="col-sm-8 input-group">
											<span class="input-group-addon">*</span> <input type="text"
												name="home_address" class="form-control"
												placeholder="Home Address">
										</div>
										<br> <br>
									</div>
									<div class="row">
										<div class="col-sm-3 col-sm-offset-2">
											<button type="submit" name="role" value="patient"
												class="btn btn-info">Submit</button>
										</div>
										<div class="col-sm-2">
											<button type="reset" class="btn btn-danger">Reset</button>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</c:if>
				<c:if test="${role=='nurse'&&operation=='add'}">
					<h1 class="page-header">Add New ${role}</h1>
					<div class="row-fluid">
						<div class="col-md-11 col-md-offset-1">
							<div class="row">
								<div class="col-md-11 col-md-offset-1"></div>
							</div>

							<div class="row-fluid">
								<div class="col-sm-3">
									<img src="img/touxiang.png" class="img-rounded">
								</div>
								<form action="PersonServlet" method="POST">
									<div class="row">
										<div class="col-sm-3">
											<div class="input-group">
												<span class="input-group-addon">*</span> <input type="text"
													name="first_name" class="form-control"
													placeholder="First Name" required="required">
											</div>
											<br>
											<div class="input-group">
												<span class="input-group-addon">*</span> <input type="text"
													name="last_name" class="form-control"
													placeholder="Last Name" required="required">
											</div>
											<br>
											<div class="col-sm-6 input-group">
												<span class="input-group-addon"> <input type="radio"
													name="gender" value="1" checked="checked">
												</span> <input type="text" readonly="readonly" class="form-control"
													value="Male">
											</div>
											<br>
											<div class="col-sm-6 input-group">
												<span class="input-group-addon"> <input type="radio"
													name="gender" value="0">
												</span><input type="text" readonly="readonly" class="form-control"
													value="Female">
											</div>
											<br>
										</div>
										<div class="col-sm-3">
											<div class="input-group">
												<span class="input-group-addon">*</span> <input type="date"
													name="birthdate" class="form-control"
													placeholder="Birth Date: yyyy-mm-dd">
											</div>
											<br>
											<div class="input-group">
												<span class="input-group-addon">*</span> <input
													type="password" name="password" class="form-control"
													placeholder="Password" required="required">
											</div>
											<br>
											<div class="input-group">
												<span class="input-group-addon">*</span> <input
													type="password" name="re-password" class="form-control"
													placeholder="Re-type Password" required="required">
											</div>
											<br>
										</div>
									</div>
									<div class="col-sm-7 col-sm-offset-3">
										<div class="col-sm-8 input-group">
											<span class="input-group-addon">*</span> <input type="email"
												name="email_address" class="form-control"
												placeholder="Email Address" required="required">
										</div>
										<br> <br>
									</div>
									<div class="row">
										<div class="col-sm-3 col-sm-offset-2">
											<button type="submit" name="role" value="nurse"
												class="btn btn-info">Submit</button>
										</div>
										<div class="col-sm-2">
											<button type="reset" class="btn btn-danger">Reset</button>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</c:if>
				<c:if test="${role=='contact'&&operation=='add'&&contact==null}">
					<h1 class="page-header">Add New ${role}</h1>
					<div class="row-fluid">
						<div class="col-md-11 col-md-offset-1">
							<div class="row">
								<div class="col-md-11 col-md-offset-1"></div>
							</div>

							<div class="row-fluid">
								<div class="col-sm-3">
									<img src="img/touxiang.png" class="img-rounded">
								</div>
								<form action="PersonServlet" method="POST">
									<div class="row">
										<div class="col-sm-3">
											<div class="input-group">
												<span class="input-group-addon">*</span> <input type="text"
													name="first_name" class="form-control"
													placeholder="First Name" required="required">
											</div>
											<br>
											<div class="input-group">
												<span class="input-group-addon">*</span> <input type="text"
													name="last_name" class="form-control"
													placeholder="Last Name" required="required">
											</div>
											<br>
											<div class="col-sm-6 input-group">
												<span class="input-group-addon"> <input type="radio"
													name="gender" value="1" checked="checked">
												</span> <input type="text" readonly="readonly" class="form-control"
													value="Male">
											</div>
											<br>
										</div>
										<div class="col-sm-3">
											<div class="input-group">
												<span class="input-group-addon">*</span> <input
													type="number" min="100001" name="patient_id"
													class="form-control" placeholder="Patient ID"
													required="required">
											</div>
											<br>
											<div class="input-group">
												<span class="input-group-addon">*</span> <input type="tel"
													pattern="^[0-9]{3}-[0-9]{3}-[0-9]{4}$" name="phone"
													class="form-control" placeholder="Phone: XXX-XXX-XXXX">
											</div>
											<br>
											<div class="col-sm-6 input-group">
												<span class="input-group-addon"> <input type="radio"
													name="gender" value="0">
												</span><input type="text" readonly="readonly" class="form-control"
													value="Female">
											</div>
											<br>
										</div>
									</div>
									<div class="col-sm-7 col-sm-offset-3">
										<div class="col-sm-8 input-group">
											<span class="input-group-addon">*</span> <input type="email"
												name="email_address" class="form-control"
												placeholder="Email Address" required="required">
										</div>
										<br> <br>
									</div>
									<div class="row">
										<div class="col-sm-3 col-sm-offset-2">
											<button type="submit" name="role" value="contact"
												class="btn btn-info">Submit</button>
										</div>
										<div class="col-sm-2">
											<button type="reset" class="btn btn-danger">Reset</button>
										</div>
									</div>
								</form>
							</div>

						</div>
					</div>
				</c:if>
				<c:if test="${role=='patient'&&operation=='edit'&&patient!=null}">
					<h1 class="page-header">Add New ${role}</h1>
					<div class="row-fluid">
						<div class="col-md-11 col-md-offset-1">
							<div class="row">
								<div class="col-md-11 col-md-offset-1"></div>
							</div>

							<div class="row-fluid">
								<div class="col-sm-3">
									<img src="img/touxiang.png" class="img-rounded">
								</div>
								<form action="PersonServlet" method="POST">
									<div class="row">
										<div class="col-sm-3">
											<div class="input-group">
												<span class="input-group-addon">*</span> <input type="text"
													name="first_name" class="form-control"
													value="${patient.firstName}" placeholder="First Name"
													required="required">
											</div>
											<br>
											<div class="input-group">
												<span class="input-group-addon">*</span> <input type="text"
													name="last_name" class="form-control"
													value="${patient.lastName}" placeholder="Last Name"
													required="required">
											</div>
											<br>

											<div class="col-sm-6 input-group">
												<span class="input-group-addon"> <input type="radio"
													name="gender" value="1"
													<c:if test="${patient.gender==1}"> checked="checked" </c:if>>
												</span> <input type="text" readonly="readonly" class="form-control"
													value="Male">
											</div>
											<br>
											<div class="col-sm-6 input-group">
												<span class="input-group-addon"> <input type="radio"
													name="gender" value="0"
													<c:if test="${patient.gender==0}"> checked="checked" </c:if>>
												</span><input type="text" readonly="readonly" class="form-control"
													value="Female">
											</div>
											<br>
										</div>
										<div class="col-sm-3">
											<div class="input-group">
												<span class="input-group-addon">*</span> <input
													type="number" min=500001 name="related_nurse_id"
													value="${patient.relatedNurseId}" class="form-control"
													placeholder="Related Nurse ID">
											</div>
											<br>
											<div class="input-group">
												<span class="input-group-addon">*</span> <input type="date"
													name="birthdate" class="form-control"
													value="${patient.birthdateStr}"
													placeholder="Birth Date: yyyy-mm-dd">
											</div>
											<br>
											<div class="input-group">
												<span class="input-group-addon">*</span> <input
													type="number" name="id" class="form-control"
													value="${patient.id}" readonly="readonly"
													placeholder="Password" required="required">
											</div>
											<br>
											<div class="input-group">
												<span class="input-group-addon">*</span> <input
													type="password" name="password" class="form-control"
													placeholder="Re-type Password" required="required">
											</div>
											<br>
										</div>
									</div>
									<div class="col-sm-7 col-sm-offset-3">
										<div class="col-sm-8 input-group">
											<span class="input-group-addon">*</span> <input type="email"
												name="email_address" class="form-control"
												value="${patient.emailAddress}" placeholder="Email Address"
												required="required">
										</div>
										<br>
									</div>
									<div class="col-sm-7 col-sm-offset-3">
										<div class="col-sm-8 input-group">
											<span class="input-group-addon">*</span> <input type="text"
												name="home_address" class="form-control"
												value="${patient.homeAddress}" placeholder="Home Address">
										</div>
										<br> <br>
									</div>
									<div class="row">
										<div class="col-sm-3 col-sm-offset-2">
											<button type="submit" name="role" value="patientUpdate"
												class="btn btn-info">Submit</button>
										</div>
										<div class="col-sm-2">
											<button type="reset" class="btn btn-danger">Reset</button>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</c:if>
				<c:if test="${role=='nurse'&&operation=='edit'&&nurse!=null}">
					<c:if test="${role=='nurse'&&operation=='edit'&&nurse!=null}">
						<h1 class="page-header">Add New ${role}</h1>
						<div class="row-fluid">
							<div class="col-md-11 col-md-offset-1">
								<div class="row">
									<div class="col-md-11 col-md-offset-1"></div>
								</div>

								<div class="row-fluid">
									<div class="col-sm-3">
										<img src="img/touxiang.png" class="img-rounded">
									</div>
									<form action="PersonServlet" method="POST">
										<div class="row">
											<div class="col-sm-3">
												<div class="input-group">
													<span class="input-group-addon">*</span> <input type="text"
														name="first_name" class="form-control"
														value="${nurse.firstName}" placeholder="First Name"
														required="required">
												</div>
												<br>
												<div class="input-group">
													<span class="input-group-addon">*</span> <input type="text"
														name="last_name" class="form-control"
														value="${nurse.lastName}" placeholder="Last Name"
														required="required">
												</div>
												<br>

												<div class="col-sm-6 input-group">
													<span class="input-group-addon"> <input type="radio"
														name="gender" value="1"
														<c:if test="${nurse.gender==1}"> checked="checked" </c:if>>
													</span> <input type="text" readonly="readonly"
														class="form-control" value="Male">
												</div>
												<br>
												<div class="col-sm-6 input-group">
													<span class="input-group-addon"> <input type="radio"
														name="gender" value="0"
														<c:if test="${nurse.gender==0}"> checked="checked" </c:if>>
													</span><input type="text" readonly="readonly" class="form-control"
														value="Female">
												</div>
												<br>
											</div>
											<div class="col-sm-3">
												<div class="input-group">
													<span class="input-group-addon">*</span> <input type="date"
														name="birthdate" class="form-control"
														value="${nurse.birthdateStr}"
														placeholder="Birth Date: yyyy-mm-dd">
												</div>
												<br>
												<div class="input-group">
													<span class="input-group-addon">*</span> <input
														type="number" name="id" class="form-control"
														value="${nurse.id}" readonly="readonly"
														placeholder="Password" required="required">
												</div>
												<br>
												<div class="input-group">
													<span class="input-group-addon">*</span> <input
														type="password" name="password" class="form-control"
														placeholder="Re-type Password" required="required">
												</div>
												<br>
											</div>
										</div>
										<div class="col-sm-7 col-sm-offset-3">
											<div class="col-sm-8 input-group">
												<span class="input-group-addon">*</span> <input type="email"
													name="email_address" class="form-control"
													value="${nurse.emailAddress}" placeholder="Email Address"
													required="required">
											</div>
											<br>
										</div>
										<div class="row">
											<div class="col-sm-3 col-sm-offset-2">
												<button type="submit" name="role" value="nurseUpate"
													class="btn btn-info">Submit</button>
											</div>
											<div class="col-sm-2">
												<button type="reset" class="btn btn-danger">Reset</button>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
					</c:if>
				</c:if>
				<c:if test="${role=='contact'&&operation=='edit'&&contact!=null}">
					<c:if test="${role=='contact'&&operation=='edit'&&contact!=null}">
						<h1 class="page-header">Add New ${role}</h1>
						<div class="row-fluid">
							<div class="col-md-11 col-md-offset-1">
								<div class="row">
									<div class="col-md-11 col-md-offset-1"></div>
								</div>

								<div class="row-fluid">
									<div class="col-sm-3">
										<img src="img/touxiang.png" class="img-rounded">
									</div>
									<form action="PersonServlet" method="POST">
										<div class="row">
											<div class="col-sm-3">
												<div class="input-group">
													<span class="input-group-addon">*</span> <input type="text"
														name="first_name" class="form-control"
														value="${contact.firstName}" placeholder="First Name"
														required="required">
												</div>
												<br>
												<div class="input-group">
													<span class="input-group-addon">*</span> <input type="text"
														name="last_name" class="form-control"
														value="${contact.lastName}" placeholder="Last Name"
														required="required">
												</div>
												<br>

												<div class="col-sm-6 input-group">
													<span class="input-group-addon"> <input type="radio"
														name="gender" value="1"
														<c:if test="${contact.gender==1}"> checked="checked" </c:if>>
													</span> <input type="text" readonly="readonly"
														class="form-control" value="Male">
												</div>
												<br>
												<div class="col-sm-6 input-group">
													<span class="input-group-addon"> <input type="radio"
														name="gender" value="0"
														<c:if test="${contact.gender==0}"> checked="checked" </c:if>>
													</span><input type="text" readonly="readonly" class="form-control"
														value="Female">
												</div>
												<br>
											</div>
											<div class="col-sm-3">
												<div class="input-group">
													<span class="input-group-addon">*</span> <input type="tel"
														name="phone" class="form-control"
														value="${contact.phoneNumber}"
														pattern="^[0-9]{3}-[0-9]{3}-[0-9]{4}$"
														placeholder="Phone: XXX-XXX-XXXX">
												</div>
												<br>
												<div class="input-group">
													<span class="input-group-addon">*</span> <input
														type="number" name="id" class="form-control"
														value="${contact.id}" readonly="readonly"
														required="required">
												</div>
												<br>
												<div class="input-group">
													<span class="input-group-addon">*</span> <input
														type="number" name="patientId" class="form-control"
														value="${patient.id}" placeholder="Patient ID"
														required="required">
												</div>
												<br>
											</div>
										</div>
										<div class="col-sm-7 col-sm-offset-3">
											<div class="col-sm-8 input-group">
												<span class="input-group-addon">*</span> <input type="email"
													name="email_address" class="form-control"
													value="${contact.emailAddress}" placeholder="Email Address"
													required="required">
											</div>
											<br>
										</div>
										<div class="row">
											<div class="col-sm-3 col-sm-offset-2">
												<button type="submit" name="role" value="contactUpate"
													class="btn btn-info">Submit</button>
											</div>
											<div class="col-sm-2">
												<button type="reset" class="btn btn-danger">Reset</button>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
					</c:if>
				</c:if>

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
										<td>${record.datetimeStr}</td>
										<td>${record.data[0]}:${record.data[1]}:${record.data[2]}<br>${record.data[3]}:${record.data[4]}:${record.data[5]}</td>
										<td>${patient.homeAddress}</td>
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
					<a href="/FallArm/PersonServlet?role=contact&operation=add"><span
						class="label label-primary">ADD CONTACT</span></a>
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
					<a href="/FallArm/PersonServlet?role=patient&operation=add"><span
						class="label label-primary">ADD PATIENT</span></a>
				</c:if>
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
					<a href="/FallArm/PersonServlet?role=contact&operation=add"><span
						class="label label-primary">ADD CONTACT</span></a>
				</c:if>
			</div>
		</div>
	</div>

</body>
</html>