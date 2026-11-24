Funktion	    HTTP	    URL

Skapa kund	POST	/customers?name=Anna&phone=123456789

Hämta kund-ID via namn	GET	/customer-id?name=Anna

Lista alla kunder + fordon	GET	/customers

Skapa fordon	POST	/vehicles?registrationNumber=ABC123&brand=Volvo&model=V60&productionYear=2022

Lista alla fordon	GET	 /vehicles

Lista fordon med visst bilmärke	GET	/vehicles-by-brand?brand=Volvo

Koppla fordon till kund	 POST	/addvehicletocustomer?customerId=1&vehicleId=1

Uppdatera bilmärke	PUT	 /vehicles/1/brand?brand=Saab

Lista registreringsnummer och bilmärke	GET	/vehicle-brand-info