//3rx
pragma solidity ^0.6.6;
contract CovidSafe{
    
    uint256 public total_account;
    uint256 public total_check;
    uint256 public total_checked;
    address public owner;
    
    constructor() public{
        owner = msg.sender;
    }
    
    modifier access(){
        require(owner == msg.sender);
        _;
    }
    
    modifier access_register(){
        require(register_account[msg.sender] == 1);
        _;
    }
    
    struct account{
        address x;
    }
    
    struct chain{
        uint256 size;
        mapping(address=>uint8) link;
        address[] list;
    }
    
    struct not_buf{
        uint8 flag; // 0 means acknowleged 1 means registered.
        string lat;
        string lon;
    }
    
    struct buffer{
        uint256 size;
        mapping(address=>not_buf) noty;
    }
    
    struct distance_chk{
        address addr1;
        address addr2;
        string lat1;
        string lat2;
        string lon1;
        string lon2;
    }
    
    mapping(string=>account) private login;
    mapping(address=>uint8) private register_account;
    mapping(address=>chain) private network;
    mapping(address=>buffer) private notify;
    mapping(address=>uint8) state; //0 means ok, 1 maeans contacted person is +ve , 2 means +ve
    mapping(uint256=>distance_chk) private check;
    
    event register_log(
        string status
    );
    
    event listener_log(
        string status
    );
    
    event state_log(
        string status
    );
    
    event positive_log(
        string status
    );
    
    event distance_log(
        string lat1,
        string lon1,
        string lat2,
        string lon2
    );
    
    event linking_log(
        string status
    );
    
    function register(string memory btAddress) public{
        if(login[btAddress].x != address(0x0000000000000000000000000000000000000000)){
            if(msg.sender == login[btAddress].x){
                emit register_log("Device already registered");
            }
            else{
                emit register_log("Device registered with another address");
            }
        }
        else{
            register_set(btAddress);
            emit register_log("Device successfully registered");
        }
    }
    
    function register_set(string memory btAddress) private{
        login[btAddress].x = msg.sender;
        register_account[msg.sender] = 1;
        total_account += 1;
    }
    
    function linking(address addr1, address addr2) private{
        if(network[addr1].link[addr2] != 1){
            network[addr1].link[addr2] = 1;
            network[addr1].list.push(addr2);
            network[addr1].size += 1;
        }
        if(network[addr2].link[addr1] != 1){
            network[addr2].link[addr1] = 1;
            network[addr2].list.push(addr1);
            network[addr2].size += 1;
        }
    }
    
    function listener(string memory btAddress, string memory lat, string memory log) public access_register{
        address addr = getAddress(btAddress);
        if(addr != address(0x0000000000000000000000000000000000000000)){
            if(notify[addr].noty[msg.sender].flag == 0){
                listener_set(addr, lat, log, 1);
                emit listener_log("Call successfully registered");
            }
            else{
                listener_set(addr, lat, log, 2);
                emit listener_log("Call successfully acknowleged");
            }
        }
        else{
            emit listener_log("Device not registered");
        }
    }
    
    function listener_set(address addr, string memory lat, string memory lon, uint8 flag) private{
        if(flag == 1){
            notify[msg.sender].noty[addr].flag = 1;
            notify[msg.sender].noty[addr].lat = lat;
            notify[msg.sender].noty[addr].lon = lon;
            notify[msg.sender].size += 1;
        }
        else{
            notify[addr].noty[msg.sender].flag = 0;
            notify[addr].size -= 1;
            update_distance(lat, lon, notify[msg.sender].noty[addr].lat, notify[msg.sender].noty[addr].lon, addr);
            linking(addr, msg.sender);
            //}
        }
    }
    
    function update_distance(string memory lat1, string memory lon1, string memory lat2, string memory lon2, address addr) private{
        check[total_check] = distance_chk(addr, msg.sender, lat1, lat2, lon1, lon2);
        total_check += 1;
    }
    
    function update_linking(uint256 id, uint8 flag) public access{
        if(flag == 1 && id == total_checked){
            linking(check[id].addr1, check[id].addr2);
            total_checked += 1;
            emit linking_log("linked successfully");
        }
    }
    
    function set_positive(address addr) public access{
        state[addr] = 2;
        emit positive_log("set successfully");
    }
    
    function getAddress(string memory btAddress) private view returns(address){
        return login[btAddress].x;
    }
    
    function getState() public{
        if(state[msg.sender] == 0){
            emit state_log("you are ok");
        }
        else if(state[msg.sender] == 1){
            emit state_log("Alert! Someone you contacted turned positive");
        }
        else if(state[msg.sender] == 2){
            emit state_log("you are positive");
        }
    }
}
