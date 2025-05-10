package com.riftco.userprofiledataserv.application.port.in;

import com.riftco.userprofiledataserv.application.SelfValidating;
import com.riftco.userprofiledataserv.domain.UserState;
import lombok.EqualsAndHashCode;
import lombok.Value;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Port for modifying existing user information.
 * Provides methods to update various user attributes.
 */
public interface ModifyUserUseCase {
    /**
     * Changes a user's display name.
     * 
     * @param command The command containing the user ID and new name
     */
    void changeName(ChangeNameCommand command);
    
    /**
     * Changes a user's email address.
     * 
     * @param command The command containing the user ID and new email
     */
    void changeEmail(ChangeEmailCommand command);
    
    /**
     * Changes a user's phone number.
     * 
     * @param command The command containing the user ID and new phone number
     */
    void changePhoneNumber(ChangePhoneNumberCommand command);
    
    /**
     * Changes a user's state (activate/deactivate).
     * 
     * @param command The command containing the user ID and the new state
     */
    void changeState(ChangeStateCommand command);

    @Value
    @EqualsAndHashCode(callSuper = false)
    class ChangeNameCommand extends SelfValidating<ChangeNameCommand> {
        @NotNull
        private UUID uuid;
        @NotEmpty
        private String name;

        public ChangeNameCommand(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;
            this.validateSelf();
        }
    }
    
    @Value
    @EqualsAndHashCode(callSuper = false)
    class ChangeEmailCommand extends SelfValidating<ChangeEmailCommand> {
        @NotNull
        private UUID uuid;
        @NotEmpty
        private String email;

        public ChangeEmailCommand(UUID uuid, String email) {
            this.uuid = uuid;
            this.email = email;
            this.validateSelf();
        }
    }
    
    @Value
    @EqualsAndHashCode(callSuper = false)
    class ChangePhoneNumberCommand extends SelfValidating<ChangePhoneNumberCommand> {
        @NotNull
        private UUID uuid;
        @NotEmpty
        private String phoneNumber;

        public ChangePhoneNumberCommand(UUID uuid, String phoneNumber) {
            this.uuid = uuid;
            this.phoneNumber = phoneNumber;
            this.validateSelf();
        }
    }
    
    @Value
    @EqualsAndHashCode(callSuper = false)
    class ChangeStateCommand extends SelfValidating<ChangeStateCommand> {
        @NotNull
        private UUID uuid;
        @NotNull
        private UserState state;

        public ChangeStateCommand(UUID uuid, UserState state) {
            this.uuid = uuid;
            this.state = state;
            this.validateSelf();
        }
        
        /**
         * Convenience factory method to create a deactivation command
         * 
         * @param uuid The user ID to deactivate
         * @return A command to deactivate the user
         */
        public static ChangeStateCommand deactivate(UUID uuid) {
            return new ChangeStateCommand(uuid, UserState.DEACTIVATED);
        }
        
        /**
         * Convenience factory method to create an activation command
         * 
         * @param uuid The user ID to activate
         * @return A command to activate the user
         */
        public static ChangeStateCommand activate(UUID uuid) {
            return new ChangeStateCommand(uuid, UserState.ACTIVATED);
        }
    }
}
