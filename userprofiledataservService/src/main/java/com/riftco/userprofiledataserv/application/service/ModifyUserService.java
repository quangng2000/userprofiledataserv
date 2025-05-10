package com.riftco.userprofiledataserv.application.service;

import com.riftco.userprofiledataserv.application.port.in.ModifyUserUseCase;
import com.riftco.userprofiledataserv.application.port.out.FindUserPort;
import com.riftco.userprofiledataserv.application.port.out.SourceUserPort;
import com.riftco.userprofiledataserv.domain.User;
import com.riftco.userprofiledataserv.domain.vo.Email;
import com.riftco.userprofiledataserv.domain.vo.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class ModifyUserService implements ModifyUserUseCase {

    private final FindUserPort findUserPort;
    private final SourceUserPort sourceUserPort;

    @Override
    @Transactional
    public void changeName(ChangeNameCommand command) {
        log.debug("Changing name for user: {}", command.getUuid());
        User user = this.findUserPort.find(command.getUuid());
        user.changeName(command.getName());
        this.sourceUserPort.source(user);
        log.info("Name changed successfully for user: {}", command.getUuid());
    }
    
    @Override
    @Transactional
    public void changeEmail(ChangeEmailCommand command) {
        log.debug("Changing email for user: {}", command.getUuid());
        User user = this.findUserPort.find(command.getUuid());
        user.changeEmail(Email.of(command.getEmail()));
        this.sourceUserPort.source(user);
        log.info("Email changed successfully for user: {}", command.getUuid());
    }
    
    @Override
    @Transactional
    public void changePhoneNumber(ChangePhoneNumberCommand command) {
        log.debug("Changing phone number for user: {}", command.getUuid());
        User user = this.findUserPort.find(command.getUuid());
        user.changePhoneNumber(PhoneNumber.of(command.getPhoneNumber()));
        this.sourceUserPort.source(user);
        log.info("Phone number changed successfully for user: {}", command.getUuid());
    }
    
    @Override
    @Transactional
    public void changeState(ChangeStateCommand command) {
        log.debug("Changing state for user: {} to {}", command.getUuid(), command.getState());
        User user = this.findUserPort.find(command.getUuid());
        
        switch (command.getState()) {
            case ACTIVATED:
                // If already activated, nothing to do
                if (!user.isActive()) {
                    // Logic to activate - might require additional implementation
                    log.warn("User activation not fully implemented");
                }
                break;
            case DEACTIVATED:
                // If already deactivated, nothing to do
                if (user.isActive()) {
                    user.deactivate();
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported user state: " + command.getState());
        }
        
        this.sourceUserPort.source(user);
        log.info("State changed successfully for user: {} to {}", command.getUuid(), command.getState());
    }
}
