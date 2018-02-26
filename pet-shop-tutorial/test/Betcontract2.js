
var account_one = "0x627306090abaB3A6e1400e9345bC60c78a8BEf57"; // an address
var account_two = "0xf17f52151EbEF6C7334FAD080c5704D77216b732"; // another address
var account_three = "0xC5fdf4076b8F3A5357c5E395ab970B5B54098Fef"; // another address

var Bet = artifacts.require("./betContractTest.sol");
var meta;

Bet.deployed().then(function (instance) {
    meta = instance;
    meta.addBetTaker(account_two);
    meta.addBetTaker(account_three);

    meta.acceptBet({from: account_two});


    //return meta.checkIfParticipantAccepted(account_two);
    return meta.checkIfParticipantAccepted(account_two);

}).then(function (accepted) {
    console.log(accepted);
    return meta.getBetState();

}).then(function(betState){
    console.log(betState.toString());
    meta.acceptBet({from: account_three})
    return meta.checkIfParticipantAccepted(account_three);


}).then(function(accept2){
    console.log(accept2);
    return meta.getBetState();


}).then(function(betState2){
    console.log(betState2.toString());
});



