<html>
<head>
<link rel="stylesheet" href="css/normalize.css" />
<link rel="stylesheet" href="css/skeleton.css" />

<script src="https://code.jquery.com/jquery-3.2.1.min.js"
	type="text/javascript"></script>
	
<script src="http://momentjs.com/downloads/moment.min.js" 
	type="text/javascript"></script>

<script>
	$(document).ready(function() {
		loadTransactions();

		$("#transactionsSearch").submit(function(e) {
			e.preventDefault();
			searchTransactions();
		});
	});

	function searchTransactions() {
		if ($('#search').val() == '') {
			loadTransactions();
		} else {
			$.ajax({
				url : 'v1/Transactions/search',
				data : { 'searchString' : $('#search').val() },
				method : 'POST',
				headers : {'content-type' : 'application/json'}
			}).done(function(data) {
				displayTransactions(data);
			});
		}
	}

	function loadTransactions() {
		$.ajax({
			url : 'v1/Transactions',
			dataType : 'json',
			method : 'GET'
		}).done(function(data) {
			displayTransactions(data);
		});
	}

	function displayTransactions(data) {
		var tbody = $('#transactions').find('tbody');
		tbody.html('');

		if (data.length > 0) {
			for (var i = 0; i < data.length; i++) {
				var transaction = data[i];

				var tr = $('<tr />');
				tr.append($('<td />').addClass('id').html(transaction.id));
				
				var date = moment(transaction.currentDate).format('DD/MM/YYYY h:mm:ss');
				
				tr.append($('<td />').html(date));
				if (transaction.supplierName == null) {
					tr.append($('<td />').html('(removed)').css('color', '#ddd'));
				} else {
					tr.append($('<td />').html(transaction.supplierName + ' (' + transaction.supplierId + ')'));
				}
				
				tr.append($('<td />').html(transaction.content));

				tbody.append(tr);
			}

			$('#transactions').show();
			$('#noTransactions').hide();
		} else {
			$('#transactions').hide();
			$('#noTransactions').show();
		}
	}
</script>

<style>
	.u-full-width {
		max-width: 1280px;
	}
	
	table td {
		font-size: 0.9em;	
	}
</style>
</head>
<body>
	<div class="container">
		<h1>Transactions</h1>
		<div class="row">
			<a href="index.jsp">&lt; Back to Suppliers</a>
		</div>

		<form id="transactionsSearch" accept-charset="utf-8">
			<div class="row">
				<div class="six columns">
					<input class="u-full-width" placeholder="Press enter to search..." id="search" type="text">
				</div>
			</div>
		</form>
		
		<p id="noTransactions" style="display: none">No transactions in system.</p>
	</div>

	<div class="container u-full-width" style="max-width: 1280px">
		<div class="row">
			<table id="transactions" style="display: none;" class="u-full-width">
				<thead>
					<tr>
						<th width="25%">Id</th>
						<th width="15%">Date</th>
						<th width="20%">Supplier</th>
						<th width="40%">Content</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
