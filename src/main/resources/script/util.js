/*
Method that will take a camelcased String and return a more readable string where the words are split and all
start with a capital letter.
*/
export const readable = (camelCasedString) => {
    const result = camelCasedString.replace( /([A-Z])/g, " $1" );
    return result.charAt(0).toUpperCase() + result.slice(1);
};

/*
Method for determining the number of significant digits to use for a number.
It is based on the hundreds, tens and single digits of the major numeric group.
For example, 125,000,000 would produce a significant digit number of 3, as there
are hundreds, tens and single values for the millions numeric group.
This method does not consider decimal places.  It is based on whole numbers only.
*/
export const significantDigits = (number) => {
    const modulus = Math.floor(number).toString().length % 3;
    if(modulus === 0)
        return 3;

    return modulus;
};