pragma solidity ^0.4.0;

import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";
import "../contracts/betContractTest.sol";

contract TestBetContract {
    betContractTest betContract = betContractTest(DeployedAddresses.betContractTest());



    function testUserAddsParticipant() public{
        address test = 0x7187c14f03656239f8cb2ab0d0f7cc3a24337d2e;
       // address test2 = 0x821aEa9a577a9b44299B9c15c88cf3087F3b5544;
        betContract.addBetTaker(test);
       // betContract.addBetProposer(test2);


        uint parts = betContract.getParticipants();

        Assert.equal(2, parts, "We have 2 participants!");
    }

    /*function testUserAcceptsBets() public{
        address test = 0x7187c14f03656239f8cb2ab0d0f7cc3a24337d2e;
        // address test2 = 0x821aEa9a577a9b44299B9c15c88cf3087F3b5544;
        betContract.addBetTaker(test);
        // betContract.addBetProposer(test2);

        betContract.acceptBet({from: test});

        bool betAccepted = betContract.checkIfParticipantAccepted(test);

        Assert.equal(true, betAccepted, "Participant hasn't accepted Bet yet");
    }*/

    function testContractConditionsDeployed() public{
        bytes32 conditions = betContract.getContractConditions();

        Assert.equal("testCond", conditions, "Conditions should match from deployment");
    }

    function testContractPrizeDeployed() public{
        uint prizePool = betContract.getContractPrizePool();

        Assert.equal(100, prizePool, "Prize Pool sbould be set to 100");
    }
}
