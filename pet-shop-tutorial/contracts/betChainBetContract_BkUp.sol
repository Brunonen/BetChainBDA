pragma solidity ^0.4.0;

contract betChainBetContractBkUp {

    bytes32 public betConditions;
    uint public betEntryFee;
    uint totalVoteCount;
    uint totalAcceptCount;
    uint public participantCount;
    uint opposerCount;
    bool public isBetSuccessfull;

    address betOwner;

    enum BetRole{Owner, Supporter, Opposer, Notar}
    enum BetState{Pending, Locked, Evaluation, Concluded, Aborted}
    BetState public currentBetState;


    modifier onlyOwnerCanAdd{ require(msg.sender == betOwner);_;}
    modifier onlyOnwerCanStartBet{ require(msg.sender == betOwner); _;}
    modifier onlyOnwerCanAbort{ require(msg.sender == betOwner); _;}
    modifier onlyContractParticipantCanExecute{ require(checkIfParticipantAlreadyEnlisted(msg.sender)); _;}

    event Bail(address _to, uint _amount);
    event BetStarted();
    event BetAborted();

    struct Participant{
        address identifier;
        bool betAccepted;
        bool voted;
        BetRole role;
    }


    mapping(address => Participant) public participants;

    address[] public participantAddresses;

    function betChainBetContractBkUp(bytes32 _betConditions, uint _betEntryFee, address[] _participantAddresses, BetRole[] _participantRoles) payable public{
        require(_betEntryFee == msg.value);
        totalAcceptCount = 1;
        totalVoteCount = 0;
        participantCount = 0;

        participants[msg.sender] = Participant(msg.sender, true, false, BetRole.Owner);
        participantAddresses.push(msg.sender);
        participantCount++;

        betConditions = _betConditions;
        betEntryFee = _betEntryFee;
        totalAcceptCount++;

        betOwner = msg.sender;
        currentBetState = BetState.Pending;
        opposerCount = 0;

        for(uint i = 0; i < _participantAddresses.length; i++){
            participants[_participantAddresses[i]] = Participant(_participantAddresses[i], false, false, _participantRoles[i]);
            participantAddresses.push(_participantAddresses[i]);
            participantCount++;
            if(_participantRoles[i] == BetRole.Opposer){
                opposerCount++;
            }
        }


    }

    function addBetSupporter(address pIdentifier) public onlyOwnerCanAdd{
        require(currentBetState == BetState.Pending);
        require(checkIfParticipantAlreadyEnlisted(pIdentifier) == false);
        participants[pIdentifier] = Participant(pIdentifier, false, false, BetRole.Supporter);
        participantAddresses.push(pIdentifier);
        participantCount++;
    }

    function addBetOpposer(address pIdentifier) public onlyOwnerCanAdd{
        require(currentBetState == BetState.Pending);
        require(checkIfParticipantAlreadyEnlisted(pIdentifier) == false);
        participants[pIdentifier] = Participant(pIdentifier, false, false, BetRole.Opposer);
        participantAddresses.push(pIdentifier);
        participantCount++;
    }

    function addBetNotar(address pIdentifier) public onlyOwnerCanAdd{
        require(currentBetState == BetState.Pending);
        require(checkIfParticipantAlreadyEnlisted(pIdentifier) == false);
        participants[pIdentifier] = Participant(pIdentifier, false, false, BetRole.Notar);
        participantAddresses.push(pIdentifier);
        participantCount++;
    }

    function checkIfParticipantAlreadyEnlisted(address participantAddress) private view returns (bool){
        if(participants[participantAddress].identifier != address(0x0)){
            return true;
        }

        return false;
    }

    function acceptBet() payable public onlyContractParticipantCanExecute returns (bool success){
        require(participants[msg.sender].betAccepted != true);

        if(participants[msg.sender].role != BetRole.Notar){
            require(msg.value >= betEntryFee);
        }

        if(participants[msg.sender].role == BetRole.Opposer){
            opposerCount++;
        }

        participants[msg.sender].betAccepted = true;

        return true;
    }

    function retreatFromBet() public onlyContractParticipantCanExecute returns (bool success){
        require(currentBetState == BetState.Pending);
        require(participants[msg.sender].role != BetRole.Notar);
        require(participants[msg.sender].betAccepted = true);
        require( address(this).balance >= betEntryFee);

        if(participants[msg.sender].role == BetRole.Opposer){
            opposerCount--;
        }

        participants[msg.sender].betAccepted = false;

        if(!msg.sender.send(betEntryFee)){
            return false;
        }

        return true;
    }

    function onlyOwnerCanExecute() public onlyOnwerCanStartBet returns (bool success){
        require(msg.sender == betOwner);
        require(currentBetState == BetState.Pending);
        require(opposerCount > 0);

        currentBetState = BetState.Locked;

        return true;
    }

    function getBetState() public view returns (BetState){
        return currentBetState;
    }

    function getBetConditions() public view returns (bytes32){
        return betConditions;
    }

    function getBetPrizePool() public view returns (uint){
        return  address(this).balance;
    }

    function getBetEntryFee() public view returns (uint){
        return betEntryFee;
    }

    function getNumberOfParticipants() public view returns (uint){
        return participantCount;
    }

    function isBetSuccessfullI() public view returns (bool){
        return isBetSuccessfull;
    }

    function getParticipantInfo(uint index) public view returns (address, bool, bool, BetRole){
        return (participants[participantAddresses[index]].identifier, participants[participantAddresses[index]].betAccepted, participants[participantAddresses[index]].voted, participants[participantAddresses[index]].role);
    }



}
