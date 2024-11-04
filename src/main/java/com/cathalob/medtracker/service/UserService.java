package com.cathalob.medtracker.service;

import com.cathalob.medtracker.dto.PractitionerRoleRequestsDTO;
import com.cathalob.medtracker.dto.UserModelDTO;
import com.cathalob.medtracker.err.PractitionerRoleRequestValidationFailed;
import com.cathalob.medtracker.err.UserAlreadyExistsException;
import com.cathalob.medtracker.model.PractitionerRoleRequest;
import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.UserRole;
import com.cathalob.medtracker.repository.PractitionerRoleRequestRepository;
import com.cathalob.medtracker.repository.UserModelRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
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
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(userModelDTO.getPassword()));
        userModelRepository.save(user);
    }

    public UserModel findByLogin(String login) {
        List<UserModel> dbUsers = findAll();
        return dbUsers.stream().filter(user -> user.getUsername().equals(login))
                .findFirst()
                .orElse(null);
    }

    public List<UserModel> findAll(){
        return StreamSupport.stream(userModelRepository.findAll().spliterator(), false)
                        .toList();
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

    public void approvePractitionerRoleRequests(String username, List<PractitionerRoleRequest> requests) {
        UserModel userModel = findByLogin(username);
        validatePractitionerRoleRequests(requests);

        requests.forEach(request -> {
            userModel.setRole(UserRole.PRACT);
            request.setApproved(true);
            practitionerRoleRequestRepository.save(request);
            saveUser(userModel);
        });


    }

    private void validatePractitionerRoleRequests  (List<PractitionerRoleRequest> requests) throws PractitionerRoleRequestValidationFailed {
        requests.forEach(request -> {
            if (!(request.getUserModel().getRole().equals(UserRole.USER))){
                throw new PractitionerRoleRequestValidationFailed("User does not have the correct role to upgrade");
            }
        });

    }



}
