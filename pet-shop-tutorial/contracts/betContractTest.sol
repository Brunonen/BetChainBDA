pragma solidity ^0.4.0;

contract betContractTest {

    bytes32 public betConditions;
    uint public prizePool;
    uint public betAcceptCount;
    uint public betConfirmCount;

    address betOwner;

    enum BetRole{ BetProposer, BetTaker, Notar}
    enum BetState{ Locked, Running, Closed, Canceled}
    BetState public currentBetState;

    modifier onlyOwnerCanAdd{ require(msg.sender == betOwner);_;}


    struct Participant{
        address identifier;
        bool betAccepted;
        bool resultConfirmed;
        BetRole role;
    }

    struct ParticipantMETA{
        address identifier;
    }

    mapping(address => Participant) public participants;
    ParticipantMETA[] public metaParts;


    function betContractTest(bytes32 _betConditions, uint _prizePool) public{
        participants[msg.sender] = Participant(msg.sender, false, false, BetRole.BetProposer);

        metaParts.push(ParticipantMETA({identifier : msg.sender}));

        participants[msg.sender].betAccepted = true;

        betConditions = _betConditions;
        prizePool = _prizePool;
        betAcceptCount = 1;

        betOwner = msg.sender;
        currentBetState = BetState.Locked;

    }

    function addBetProposer(address pIdentifier) public onlyOwnerCanAdd{
        require(currentBetState == BetState.Locked);
        require(checkIfParticipantAlreadyEnlisted(pIdentifier) == false);
        participants[pIdentifier] = Participant(pIdentifier, false, false, BetRole.BetProposer);

        metaParts.push(ParticipantMETA({identifier : pIdentifier}));
    }

    function addBetTaker(address pIdentifier) public onlyOwnerCanAdd{
        require(currentBetState == BetState.Locked);
        require(checkIfParticipantAlreadyEnlisted(pIdentifier) == false);
        participants[pIdentifier] = Participant(pIdentifier, false, false, BetRole.BetTaker);

        metaParts.push(ParticipantMETA({identifier : pIdentifier}));
    }

    function addBetNotar(address pIdentifier) public onlyOwnerCanAdd{
        require(currentBetState == BetState.Locked);
        require(checkIfParticipantAlreadyEnlisted(pIdentifier) == false);
        participants[pIdentifier] = Participant(pIdentifier, false, false, BetRole.Notar);

        metaParts.push(ParticipantMETA({identifier : pIdentifier}));
    }

    function checkIfParticipantAccepted(address participant) public view returns (bool){
        return participants[participant].betAccepted;
    }

    function checkIfParticipantAlreadyEnlisted(address participant) returns (bool){
        for(uint i = 0; i < metaParts.length; i++){
            if(metaParts[i].identifier == participant){
                return true;
            }
        }
        return false;
    }

    function acceptBet() public{
        require(participants[msg.sender].betAccepted != true);


        participants[msg.sender].betAccepted = true;

        betAcceptCount++;

        if(betAcceptCount == metaParts.length){
            currentBetState = BetState.Running;
        }
    }

    function getParticipants() public view returns (uint){
        address[] registeredParticipants;
        uint addressCount;

        for(uint i = 0; i < metaParts.length; i++){
            address currentIdentifier = metaParts[i].identifier;
            registeredParticipants.push(currentIdentifier);
            addressCount++;
        }

        return addressCount;
    }

    function getBetState() public view returns (BetState){
        return currentBetState;
    }

    function getContractConditions() public view returns (bytes32){
        return betConditions;
    }

    function getContractPrizePool() public view returns (uint){
        return prizePool;
    }

}
