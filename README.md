# Generic Bar Chart Application with D3

This is a Spring Boot based web application that serves up static content that will 
generate a D3 Bar Chart based on the selection by the user from a drop down menu.  

The server side provides the javascript, via an API call, the name and type of all the 
fields available from the configured data store, in this case a CSV file but 
certainly can use any data store that implements the "Data" interface.  The 
javascript then filters out any fields that are not an 'int' or 'float' from the 
available options the user can choose from to graph, as a Bar Chart cannot display
non-numeric data.  Using the appropriate values, it builds a drop down menu that 
the user can use to toggle between displayed data in the bar chart.  

The Y Axis label does change as the user picks a different option from the dropdown menu.

