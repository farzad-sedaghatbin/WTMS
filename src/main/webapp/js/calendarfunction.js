/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function(){
    $(".datepicker").datepicker(
    { 
        dateFormat: "yy/mm/dd",
        showOn: "button",
        buttonImage: "../../../resources/images/s_calendar.png",
        buttonImageOnly: true
    });
});