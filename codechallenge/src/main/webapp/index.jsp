<html>
<head>
<link rel="stylesheet" href="css/normalize.css" />
<link rel="stylesheet" href="css/skeleton.css" />

<script src="https://code.jquery.com/jquery-3.2.1.min.js"
	type="text/javascript"></script>

<script>
	$(document).ready(function() {
		loadSuppliers();
		
		$("#supplierForm").submit(function(e) {
		    e.preventDefault();
		    saveSupplier();
		});
	});

	function deleteSupplier(id) {
		return function() {
			$.ajax({
				url: 'v1/Suppliers/' + id,
				method: 'DELETE',
				headers: { 'content-type': 'application/json' } 
			}).done(function() {
				loadSuppliers();
			});
		}
	}

	var editId = 0;
	function editSupplier(id, name, address, contact) {
		return function() {
			$('#name').val(name);
			$('#contact').val(contact);
			$('#address').val(address);
			
			editId = id;
		}
	}
	
	function saveSupplier() {
		var obj = {
			name: $('#name').val(),
			address: $('#address').val(),
			contact: $('#contact').val().replace(/[^\d\.\-\+]/g, '')
		};
		
		if (editId > 0) {
			obj.id = editId;
		}
		
		$.ajax({
			url: 'v1/Suppliers/',
			method: 'POST',
			headers: { 'content-type': 'application/json' },
			data: JSON.stringify(obj)
		}).done(function(d) {
			editId = 0;
			loadSuppliers();
		});
	}
	
	function loadSuppliers() {
		$('#name').val('');
		$('#contact').val('');
		$('#address').val('');
		
		$.ajax({
			url : 'v1/Suppliers',
			dataType : 'json',
			method : 'GET'
		}).done(function(data) {
			var tbody = $('#suppliers').find('tbody');
			tbody.html('');

			if (data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					var supplier = data[i];

					var tr = $('<tr />');
					tr.append($('<td />').html(supplier.id));
					tr.append($('<td />').html(supplier.name));
					tr.append($('<td />').html(supplier.address));
					tr.append($('<td />').html(supplier.contact));

					var deleteBtn = $('<button />').html('Delete');
					deleteBtn.bind('click', deleteSupplier(supplier.id));

					var editBtn = $('<button />').html('Edit');
					editBtn.bind('click', editSupplier(supplier.id, supplier.name, supplier.address, supplier.contact));

					tr.append($('<td />').append(deleteBtn).append("&nbsp;").append(editBtn));

					tbody.append(tr);
				}

				$('#suppliers').show();
				$('#noSuppliers').hide();
			} else {
				$('#suppliers').hide();
				$('#noSuppliers').show();
			}
		});
	}
</script>
</head>
<body>
	<div class="container">
		<h1>Suppliers</h1>
		<div class="row"><a href="transactions.jsp">View Transactions &gt;</a></div>
		<div class="row">
			<table id="suppliers" style="display: none;" class="u-full-width">
				<thead>
					<tr>
						<th>Id</th>
						<th>Name</th>
						<th>Address</th>
						<th>Contact</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<p id="noSuppliers" style="display: none">No suppliers in system.</p>
		</div>

		<h2>Add Supplier</h2>
		<form id="supplierForm" accept-charset="utf-8">
			<div class="row">
				<div class="six columns">
					<label for="name">Name</label> <input class="u-full-width"
						placeholder="John Smith" id="name" type="text" required>
				</div>
				<div class="six columns">
					<label for="contact">Contact Number</label> <input class="u-full-width"
						placeholder="+1-202-555-0115" id="contact" type="tel">
				</div>
			</div>
			<label for="address">Address</label>
			<textarea class="u-full-width" id="address" required></textarea>
			<input class="button-primary" value="Submit" type="submit">
		</form>
	</div>
</body>
</html>
