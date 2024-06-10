<h1>Currency convertor</h1>

<h3>Create a CLI application that integrates with Fast Forex and lets the user convert currencies with exchange rates from past dates.
Requirements:</h3>

* The application must accept a command line argument for the date in format '2024-12-31'.
* The application must be able to process multiple conversions.
* The application must continuously validate all inputs until a correct one is submitted. Мonetary values should be constrained to two decimal places. Currencies must be in ISO 4217 three letter currency code format.
* The application must be case-insensitive.
* The application must cache the exchange rates for each requested base currency. Subsequent conversions with this base currency should use the cached data, instead of calling the API.
* Each successful conversion must be saved in a json file with the provided format.
* The application must be terminated by typing 'END' on any input.
* The application must load the ${\color{red}ApiKey}$ for ${\color{blue}Fast Forex}$ from a ${\color{red}config.json}$ file which must be ignored by the version control.
* The executable must be named CurrencyConversion.


<h3>Example:</h3>

The application must accept input for Amount, Base currency and Target currency in the given order.
* Input in the example is in ${\color{green}GREEN}$ ${\color{green}COLOR}$ and in the order of ${\color{green}Amount, Base Currency, Target Currency}$ every single time.
* Output in the example is BOLD.

<a href="https://ibb.co/f1DdydN"><img src="https://i.ibb.co/JB3jGjp/2024-06-10-155453.png" alt="2024-06-10-155453" border="0"></a>
