var Adoption = artifacts.require("Adoption");

module.exports = function(deployer) {
    deployer.deploy(Adoption);
};

var betContractTest = artifacts.require("betContractTest");

module.exports = function(deployer){
    deployer.deploy(betContractTest, "testCond", 100);
}