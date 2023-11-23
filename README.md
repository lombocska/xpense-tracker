# Expense tracker app


Tutorial videos can be found [here](https://www.youtube.com/watch?v=EN0wjY99LHo&list=PLKQd0LVFAwQaSLlXl5mm5p40KaWtB3J2J)

<div class="image-container">
  <img src="https://raw.githubusercontent.com/lombocska/xpense-tracker/master/documentation/img/login.png" width="200" height="400">
  <img src="https://raw.githubusercontent.com/lombocska/xpense-tracker/master/documentation/img/home.png" width="200" height="400">
  <img src="https://raw.githubusercontent.com/lombocska/xpense-tracker/master/documentation/img/date-range-picker.png" width="200" height="400">
  <img src="https://raw.githubusercontent.com/lombocska/xpense-tracker/master/documentation/img/add-transaction.png" width="200" height="400">
  <img src="https://raw.githubusercontent.com/lombocska/xpense-tracker/master/documentation/img/date-picker.png" width="200" height="400">
  <img src="https://raw.githubusercontent.com/lombocska/xpense-tracker/master/documentation/img/stat.png" width="200" height="400">
  <img src="https://raw.githubusercontent.com/lombocska/xpense-tracker/master/documentation/img/settings.png" width="200" height="400">
</div>

## Features

- login with username and password
- main screen with the monthly income and expenses,filtering possibilities and the transaction list of them
- opportunity to add new income or expense with custom date and notes, different categories
- in stat page 2 different charts show real values comparison (PieChart, grouped BarChart)
- in settings page, currency can be changed

## Login

This feature was implemented with Android Studio Built-in Login Activity. The users are managed via 
LoginRepository and LoginDataSource with SQLite db. In AndroidManifests.xml, login activity is exported 
and get to be the main activity as an activity intent. 




