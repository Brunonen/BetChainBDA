pragma solidity ^0.4.19;

contract betChainBetContract {

    string public betConditions;
    uint public betEntryFee;
    uint failureCount;
    uint successfulCount;
    uint totalVoteCount;
    uint totalAcceptCount;
    uint public participantCount;
    uint opposerCount;
    uint supporterCount;
    uint notarCount;
    uint abortCount;
    uint prizePool;
    bool public isBetSuccessfull;

    address betOwner;

    enum BetRole{Owner, Supporter, Opposer, Notar}
    enum BetState{Pending, Locked, Evaluation, Concluded, Aborted}

    BetState public currentBetState;

    modifier onlyOwnerCanExecute{ require(msg.sender == betOwner); _;}
    modifier onlyContractParticipantCanExecute{ require(checkIfParticipantAlreadyEnlisted(msg.sender)); _;}

    struct Participant{
        address identifier;
        bool betAccepted;
        bool voted;
        bool abortVoted;
        BetRole role;
    }


    mapping(address => Participant) public participants;

    address[] public participantAddresses;

    function betChainBetContract(string _betConditions, uint _betEntryFee, address[] _participantAddresses, BetRole[] _participantRoles) payable public{
        require(_betEntryFee == msg.value);
        participantCount = 0;

        participants[msg.sender] = Participant(msg.sender, true, false, false, BetRole.Owner);
        participantAddresses.push(msg.sender);
        participantCount++;
        totalAcceptCount = 1;
        totalVoteCount = 0;
        prizePool = _betEntryFee;

        betConditions = _betConditions;
        betEntryFee = _betEntryFee;

        betOwner = msg.sender;
        currentBetState = BetState.Pending;
        opposerCount = 0;
        supporterCount = 1;
        notarCount = 0;

        for(uint i = 0; i < _participantAddresses.length; i++){
            participants[_participantAddresses[i]] = Participant(_participantAddresses[i], false, false, false, _participantRoles[i]);
            participantAddresses.push(_participantAddresses[i]);
            participantCount++;
        }
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
            prizePool += betEntryFee;
        }

        if(participants[msg.sender].role == BetRole.Opposer){
            opposerCount++;
        }

        if(participants[msg.sender].role == BetRole.Supporter){
            supporterCount++;
        }

        if(participants[msg.sender].role == BetRole.Notar){
            notarCount++;
        }

        participants[msg.sender].betAccepted = true;

        totalAcceptCount++;



        if(totalAcceptCount == participantCount){
            currentBetState = BetState.Locked;
        }

        return true;
    }

    function retreatFromBet() public onlyContractParticipantCanExecute returns (bool success){
        require(currentBetState == BetState.Pending);
        require(participants[msg.sender].role != BetRole.Notar);
        require(participants[msg.sender].betAccepted = true);
        require( address(this).balance >= betEntryFee);

        if(participants[msg.sender].role == BetRole.Opposer){
            opposerCount--;
            prizePool -= betEntryFee;
        }

        if(participants[msg.sender].role == BetRole.Supporter){
            supporterCount--;
            prizePool -= betEntryFee;
        }

        if(participants[msg.sender].role == BetRole.Notar){
            notarCount--;
        }

        participants[msg.sender].betAccepted = false;

        if(!msg.sender.send(betEntryFee)){
            return false;
        }

        totalAcceptCount--;


        return true;
    }

    function startBet() public onlyOwnerCanExecute returns (bool success){
        require(currentBetState == BetState.Pending);
        require(opposerCount > 0);

        currentBetState = BetState.Locked;

        return true;
    }

    function startVote(bool betSuccessfull) public onlyOwnerCanExecute returns (bool success){
        require(currentBetState == BetState.Locked);

        if(betSuccessfull){
            successfulCount++;
        }else{
            failureCount++;
        }

        participants[msg.sender].voted = true;
        totalVoteCount++;

        currentBetState = BetState.Evaluation;

        return true;

    }

    function vote(bool betSuccessfull) public onlyContractParticipantCanExecute returns (bool success){
        require(currentBetState == BetState.Evaluation);
        require(participants[msg.sender].betAccepted == true);
        require(participants[msg.sender].voted == false);

        uint biggestParty = 0;

        if(opposerCount >= supporterCount){
            biggestParty = opposerCount;
        }else{
            biggestParty = supporterCount;
        }

        if(participants[msg.sender].role != BetRole.Notar){
            if(betSuccessfull){
                successfulCount++;
            }else{
                failureCount++;
            }
        }else{
            if(betSuccessfull){
                successfulCount += (biggestParty / notarCount);
            }else{
                failureCount += (biggestParty / notarCount);
            }
        }

        totalVoteCount++;
        participants[msg.sender].voted = true;

        if(totalVoteCount == totalAcceptCount){
            if(successfulCount == failureCount){
                successfulCount = 0;
                failureCount = 0;
                totalVoteCount = 0;
                currentBetState = BetState.Locked;

                for(uint n = 0; n < participantAddresses.length; n++){
                    participants[participantAddresses[n]].voted = false;
                }

                return true;
            }else if(successfulCount > failureCount){
                isBetSuccessfull = true;
            }else{
                isBetSuccessfull = false;
            }

            currentBetState = BetState.Concluded;

            uint prizeMoney = 0;
            if(isBetSuccessfull){
                prizeMoney = address(this).balance / supporterCount;
            }else{
                prizeMoney = address(this).balance / opposerCount;
            }

            for(uint i = 0; i < participantAddresses.length; i++){
                if(isBetSuccessfull){
                    if(participants[participantAddresses[i]].role == BetRole.Supporter || participants[participantAddresses[i]].role == BetRole.Owner && participants[participantAddresses[i]].betAccepted == true){
                        if(!participantAddresses[i].send(prizeMoney)){

                        }
                    }
                }else{
                    if(participants[participantAddresses[i]].role == BetRole.Opposer && participants[participantAddresses[i]].betAccepted == true){
                        if(!participantAddresses[i].send(prizeMoney)){

                        }
                    }
                }
            }
        }
        return true;
    }

    function abortBet() public onlyContractParticipantCanExecute returns (bool success){
        require(currentBetState == BetState.Pending || currentBetState == BetState.Locked || currentBetState == BetState.Evaluation);
        require(participants[msg.sender].abortVoted == false);
        require(participants[msg.sender].betAccepted == true);

        abortCount++;
        participants[msg.sender].abortVoted = true;

        if(abortCount == totalAcceptCount){
            currentBetState = BetState.Aborted;

            for(uint i = 0; i < participantAddresses.length; i++){
                if(participants[participantAddresses[i]].role != BetRole.Notar){
                    if(!participantAddresses[i].send(betEntryFee)){

                    }
                }
            }
        }

        return true;
    }

    function getBetState() public view returns (BetState){
        return currentBetState;
    }

    function getBetConditions() public view returns (string){
        return betConditions;
    }

    function getBetPrizePool() public view returns (uint){
        return  prizePool;
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

    function getParticipantInfo(uint index) public view returns (address, bool, bool, bool, BetRole){
        return (participants[participantAddresses[index]].identifier, participants[participantAddresses[index]].betAccepted, participants[participantAddresses[index]].voted, participants[participantAddresses[index]].abortVoted, participants[participantAddresses[index]].role);
    }

}
