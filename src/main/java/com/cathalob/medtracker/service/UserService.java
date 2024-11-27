package com.cathalob.medtracker.service;

import com.cathalob.medtracker.dto.PractitionerRoleRequestsDTO;
import com.cathalob.medtracker.dto.UserModelDTO;
import com.cathalob.medtracker.err.PractitionerRoleRequestValidationFailed;
import com.cathalob.medtracker.err.UserAlreadyExistsException;
import com.cathalob.medtracker.model.PractitionerRoleRequest;
import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.enums.USERROLE;
import com.cathalob.medtracker.repository.PractitionerRoleRequestRepository;
import com.cathalob.medtracker.repository.UserModelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {


    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserModelRepository userModelRepository;
    @Autowired
    PractitionerRoleRequestRepository practitionerRoleRequestRepository;

    public void register(UserModelDTO userModelDTO) throws UserAlreadyExistsException {
        if (findByLogin(userModelDTO.getUsername()) != null){
            throw new UserAlreadyExistsException("User with email already exists");
        }

        UserModel user = new UserModel();
        user.setUsername(userModelDTO.getUsername());
        user.setRole(USERROLE.USER);
        user.setPassword(passwordEncoder.encode(userModelDTO.getPassword()));
        userModelRepository.save(user);
    }

    public UserModel findByLogin(String login) {
        List<UserModel> dbUsers = getUserModels();
        return dbUsers.stream().filter(user -> user.getUsername().equals(login))
                .findFirst()
                .orElse(null);
    }

    public List<UserModel> getUserModels(){
        return userModelRepository.findAll();
    }
    public Map<Integer, UserModel> getUserModelsById(){
        return getUserModels()
                .stream().collect(Collectors.toMap(UserModel::getId, Function.identity()));
    }

//    Updating
    public void saveUser(UserModel userModel){
        userModelRepository.save(userModel);
    }




//    USER Role functions
    public void submitPractitionerRoleRequest(String username) {
        UserModel userModel = findByLogin(username);
        PractitionerRoleRequest practitionerRoleRequest = new PractitionerRoleRequest();
        practitionerRoleRequest.setUserModel(userModel);
        practitionerRoleRequestRepository.save(practitionerRoleRequest);
    }
    public PractitionerRoleRequest getPractitionerRoleRequest(String username){
        UserModel userModel = findByLogin(username);
        return practitionerRoleRequestRepository.findById(userModel.getId()).orElse(null);

    }




//ADMIN user functions
    public List<PractitionerRoleRequest> getPractitionerRoleRequests() {
    return StreamSupport.stream(practitionerRoleRequestRepository.findAll().spliterator(), false)
            .toList();

}

    public PractitionerRoleRequestsDTO getPractitionerRoleRequestsDTO() {
        return new PractitionerRoleRequestsDTO(getPractitionerRoleRequests());
    }

    public void approvePractitionerRoleRequests( List<PractitionerRoleRequest> requests) {

        List<PractitionerRoleRequest> toProcess = assembledRoleRequestsToProcess(requests);

        validatePractitionerRoleRequests(toProcess);
        toProcess.forEach(request -> {
            log.info("processing req for: " + request.getUserModel().getUsername() + request.getUserModel().getId());
            if (request.isApproved()) {
                request.getUserModel().setRole(USERROLE.PRACT);
            }
            else {
                request.getUserModel().setRole(USERROLE.USER);
            }
            request.setId(request.getUserModel().getId());
            if (request.isApproved()){
                practitionerRoleRequestRepository.save(request);
            } else {
                practitionerRoleRequestRepository.delete(request);
            }
            saveUser(request.getUserModel());
        });

    }


    private List<PractitionerRoleRequest> assembledRoleRequestsToProcess(List<PractitionerRoleRequest> requests) throws PractitionerRoleRequestValidationFailed {
        List<PractitionerRoleRequest> toProcess = new ArrayList<>();
        requests.forEach(request -> {
            String username = request.getUserModel().getUsername();
            log.info("assembling req for: " + username);
            UserModel existingUser = findByLogin(username);
            if ((existingUser == null)) {
                throw new PractitionerRoleRequestValidationFailed("Non existent user detected: " + username);
            }

            request.setUserModel(existingUser);
            if ((request.isApproved() && request.getUserModel().getRole().equals(USERROLE.USER)) || (!request.isApproved() && request.getUserModel().getRole().equals(USERROLE.PRACT))) {
                toProcess.add(request);
            }
        });
        return toProcess;
    }

    private void validatePractitionerRoleRequests  (List<PractitionerRoleRequest> requests) throws PractitionerRoleRequestValidationFailed {
        if (requests.isEmpty()) {
            throw new PractitionerRoleRequestValidationFailed("No valid changes detected");
        }
        requests.forEach(request -> {
            log.info("validating req for: " + request.getUserModel().getUsername() + request.getUserModel().getId());
            if ((request.getUserModel().getId() == null)){
                throw new PractitionerRoleRequestValidationFailed("User not found: " + request.getUserModel().getUsername());
            }
            if (!(request.getUserModel().getRole().equals(USERROLE.USER)) && request.isApproved()){
                throw new PractitionerRoleRequestValidationFailed("User does not have the correct role to upgrade: "+ request.getUserModel().getUsername());
            }
            if (!(request.getUserModel().getRole().equals(USERROLE.PRACT)) && !(request.isApproved())){
                throw new PractitionerRoleRequestValidationFailed("User does not have the correct role to downgrade: " + request.getUserModel().getUsername() );
            }
        });

    }



}
