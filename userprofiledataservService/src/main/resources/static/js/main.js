/**
 * RiftCo User Profile Management - Main JavaScript
 * Handles UI navigation, API calls, and form submissions
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize page navigation
    initPageNavigation();
    
    // Initialize form submissions
    initForms();
    
    // Initialize UUID generation buttons
    initUuidGenerators();
});

/**
 * Initialize page navigation
 */
function initPageNavigation() {
    const navLinks = document.querySelectorAll('a[data-page]');
    
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const targetPage = this.getAttribute('data-page');
            showPage(targetPage);
            
            // Update active nav link
            document.querySelectorAll('.nav-link').forEach(navLink => {
                navLink.classList.remove('active');
            });
            this.classList.add('active');
        });
    });
}

/**
 * Show a specific page and hide others
 */
function showPage(pageId) {
    document.querySelectorAll('.page').forEach(page => {
        page.classList.remove('active');
    });
    
    const targetPage = document.getElementById(pageId);
    if (targetPage) {
        targetPage.classList.add('active');
    }
}

/**
 * Initialize form submissions
 */
function initForms() {
    // Create Tenant Form
    const createTenantForm = document.getElementById('create-tenant-form');
    if (createTenantForm) {
        createTenantForm.addEventListener('submit', handleCreateTenant);
    }
    
    // Create User Form
    const createUserForm = document.getElementById('create-user-form');
    if (createUserForm) {
        createUserForm.addEventListener('submit', handleCreateUser);
    }
    
    // Create Profile Form
    const createProfileForm = document.getElementById('create-profile-form');
    if (createProfileForm) {
        createProfileForm.addEventListener('submit', handleCreateProfile);
    }
    
    // View Profile Form
    const viewProfileForm = document.getElementById('view-profile-form');
    if (viewProfileForm) {
        viewProfileForm.addEventListener('submit', handleViewProfile);
    }
    
    // View Profile By User and Tenant Form
    const viewProfileByUserTenantForm = document.getElementById('view-profile-by-user-tenant-form');
    if (viewProfileByUserTenantForm) {
        viewProfileByUserTenantForm.addEventListener('submit', handleViewProfileByUserAndTenant);
    }
    
    // List Profiles Form
    const listProfilesForm = document.getElementById('list-profiles-form');
    if (listProfilesForm) {
        listProfilesForm.addEventListener('submit', handleListProfiles);
    }
    
    // Lookup Tenant Form
    const lookupTenantForm = document.getElementById('lookup-tenant-form');
    if (lookupTenantForm) {
        lookupTenantForm.addEventListener('submit', handleLookupTenant);
    }
    
    // Modify Tenant Form
    const modifyTenantForm = document.getElementById('modify-tenant-form');
    if (modifyTenantForm) {
        modifyTenantForm.addEventListener('submit', handleModifyTenant);
    }
    
    // Lookup User Form
    const lookupUserForm = document.getElementById('lookup-user-form');
    if (lookupUserForm) {
        lookupUserForm.addEventListener('submit', handleLookupUser);
    }
    
    // Modify User Form
    const modifyUserForm = document.getElementById('modify-user-form');
    if (modifyUserForm) {
        modifyUserForm.addEventListener('submit', handleModifyUser);
    }
    
    // Lookup Profile Form
    const lookupProfileForm = document.getElementById('lookup-profile-form');
    if (lookupProfileForm) {
        lookupProfileForm.addEventListener('submit', handleLookupProfile);
    }
    
    // Modify Profile Form
    const modifyProfileForm = document.getElementById('modify-profile-form');
    if (modifyProfileForm) {
        modifyProfileForm.addEventListener('submit', handleModifyProfile);
    }
}

/**
 * Handle create tenant form submission
 */
function handleCreateTenant(e) {
    e.preventDefault();
    
    // Get form values
    const name = document.getElementById('tenantName').value;
    const description = document.getElementById('tenantDescription').value;
    const tenantType = document.getElementById('tenantType').value;
    const status = document.getElementById('tenantStatus').value;
    const subscriptionPlanType = document.getElementById('subscriptionPlanType').value;
    
    // Create tenant data object
    const tenantData = {
        name: name,
        description: description || null,
        tenantType: tenantType,
        status: status || null,
        subscriptionPlanType: subscriptionPlanType || null
    };
    
    // Send API request
    fetch('/v1/tenants', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(tenantData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // Show success message
        showAlert('success', `Tenant created successfully with ID: ${data.id}`);
        
        // Reset form
        document.getElementById('create-tenant-form').reset();
    })
    .catch(error => {
        console.error('Error creating tenant:', error);
        showAlert('danger', 'Error creating tenant: ' + error.message);
    });
}

/**
 * Handle create user form submission
 */
function handleCreateUser(e) {
    e.preventDefault();
    
    // Get form values
    const tenantId = document.getElementById('userTenantId').value;
    const name = document.getElementById('userName').value;
    const email = document.getElementById('userEmail').value;
    const contactNumber = document.getElementById('userContactNumber').value;
    
    // Create user data object
    const userData = {
        tenantId: tenantId,
        name: name,
        email: email,
        contactNumber: contactNumber
    };
    
    // Send API request
    fetch('/v1/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // Show success message
        showAlert('success', `User created successfully with ID: ${data.id}`);
        
        // Reset form
        document.getElementById('create-user-form').reset();
    })
    .catch(error => {
        console.error('Error creating user:', error);
        showAlert('danger', 'Error creating user: ' + error.message);
    });
}

/**
 * Handle create profile form submission
 */
function handleCreateProfile(e) {
    e.preventDefault();
    
    // Get form values
    const userId = document.getElementById('userId').value;
    const tenantId = document.getElementById('tenantId').value;
    const displayName = document.getElementById('displayName').value;
    const avatarUrl = document.getElementById('avatarUrl').value;
    const biography = document.getElementById('biography').value;
    const jobTitle = document.getElementById('jobTitle').value;
    const department = document.getElementById('department').value;
    const location = document.getElementById('location').value;
    const linkedInUrl = document.getElementById('linkedInUrl').value;
    const twitterUrl = document.getElementById('twitterUrl').value;
    const gitHubUrl = document.getElementById('gitHubUrl').value;
    
    // Create profile data object
    const profileData = {
        userId: userId,
        tenantId: tenantId,
        displayName: displayName,
        avatarUrl: avatarUrl || null,
        biography: biography || null,
        jobTitle: jobTitle || null,
        department: department || null,
        location: location || null,
        linkedInUrl: linkedInUrl || null,
        twitterUrl: twitterUrl || null,
        gitHubUrl: gitHubUrl || null
    };
    
    // Send API request
    fetch('/v1/profiles', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(profileData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // Show success message
        showAlert('success', `Profile created successfully with ID: ${data.id}`);
        
        // Reset form
        document.getElementById('create-profile-form').reset();
    })
    .catch(error => {
        console.error('Error creating profile:', error);
        showAlert('danger', 'Error creating profile: ' + error.message);
    });
}

/**
 * Handle view profile form submission
 */
function handleViewProfile(e) {
    e.preventDefault();
    
    // Get profile ID
    const profileId = document.getElementById('profileId').value;
    
    // Send API request
    fetch(`/v1/profiles/${profileId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Profile not found');
        }
        return response.json();
    })
    .then(data => {
        // Display profile details
        displayProfileDetails(data);
    })
    .catch(error => {
        console.error('Error fetching profile:', error);
        showAlert('danger', 'Error fetching profile: ' + error.message);
        
        // Hide profile details
        document.getElementById('profile-details').classList.add('d-none');
    });
}

/**
 * Handle view profile by user ID and tenant ID form submission
 */
function handleViewProfileByUserAndTenant(e) {
    e.preventDefault();
    
    // Get user ID and tenant ID
    const userId = document.getElementById('viewUserId').value;
    const tenantId = document.getElementById('viewTenantId').value;
    
    // Send API request to the correct endpoint
    fetch(`/v1/profiles/user/${userId}/tenant/${tenantId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Profile not found');
        }
        return response.json();
    })
    .then(data => {
        // Display profile details
        displayProfileDetails(data);
        
        // Show the profile details section
        document.getElementById('profile-details').classList.remove('d-none');
    })
    .catch(error => {
        console.error('Error fetching profile:', error);
        showAlert('danger', 'Error fetching profile: ' + error.message);
        
        // Hide profile details
        document.getElementById('profile-details').classList.add('d-none');
    });
}

/**
 * Handle list profiles form submission
 */
function handleListProfiles(e) {
    e.preventDefault();
    
    const tenantId = document.getElementById('filterTenantId').value;
    
    // Send API request
    fetch(`/v1/profiles/tenant/${tenantId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to fetch profiles');
        }
        return response.json();
    })
    .then(data => {
        // Display profiles list
        displayProfilesList(data);
        
        // Show the profiles table
        document.getElementById('profiles-table').classList.remove('d-none');
    })
    .catch(error => {
        console.error('Error fetching profiles:', error);
        showAlert('danger', 'Error fetching profiles: ' + error.message);
        
        // Hide profiles table
        document.getElementById('profiles-table').classList.add('d-none');
        document.getElementById('no-profiles-message').classList.add('d-none');
    });
}

/**
 * Handle lookup tenant form submission
 */
function handleLookupTenant(e) {
    e.preventDefault();
    
    const tenantId = document.getElementById('lookupTenantId').value;
    
    // Send API request to get tenant details
    fetch(`/v1/tenants/${tenantId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Tenant not found');
        }
        return response.json();
    })
    .then(data => {
        // Store tenant ID in hidden field
        document.getElementById('modifyTenantId').value = tenantId;
        
        // Populate form with tenant data
        document.getElementById('modifyTenantName').value = data.name || '';
        document.getElementById('modifyTenantDescription').value = data.description || '';
        document.getElementById('modifyTenantStatus').value = data.status || '';
        document.getElementById('modifySubscriptionPlanType').value = data.subscriptionPlanType || '';
        
        // Show the form container
        document.getElementById('modify-tenant-form-container').classList.remove('d-none');
        
        showAlert('success', 'Tenant found! You can now modify its details.');
    })
    .catch(error => {
        console.error('Error fetching tenant:', error);
        showAlert('danger', 'Error fetching tenant: ' + error.message);
        
        // Hide the form container
        document.getElementById('modify-tenant-form-container').classList.add('d-none');
    });
}

/**
 * Handle modify tenant form submission
 */
function handleModifyTenant(e) {
    e.preventDefault();
    
    const tenantId = document.getElementById('modifyTenantId').value;
    
    // Build request body with only non-empty fields
    const requestBody = {};
    
    const name = document.getElementById('modifyTenantName').value.trim();
    if (name) {
        requestBody.name = name;
    }
    
    const description = document.getElementById('modifyTenantDescription').value.trim();
    if (description) {
        requestBody.description = description;
    }
    
    const status = document.getElementById('modifyTenantStatus').value;
    if (status) {
        requestBody.status = status;
    }
    
    const subscriptionPlanType = document.getElementById('modifySubscriptionPlanType').value.trim();
    if (subscriptionPlanType) {
        requestBody.subscriptionPlanType = subscriptionPlanType;
    }
    
    // Check if any field is populated
    if (Object.keys(requestBody).length === 0) {
        showAlert('warning', 'Please fill at least one field to update.');
        return;
    }
    
    // Send API request
    fetch(`/v1/tenants/${tenantId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to update tenant');
        }
        return response.json();
    })
    .then(data => {
        showAlert('success', 'Tenant updated successfully!');
    })
    .catch(error => {
        console.error('Error updating tenant:', error);
        showAlert('danger', 'Error updating tenant: ' + error.message);
    });
}

/**
 * Handle lookup user form submission
 */
function handleLookupUser(e) {
    e.preventDefault();
    
    const userId = document.getElementById('lookupUserId').value;
    
    // Send API request to get user details
    fetch(`/v1/users/${userId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('User not found');
        }
        return response.json();
    })
    .then(data => {
        // Store user ID in hidden field
        document.getElementById('modifyUserId').value = userId;
        
        // Populate form with user data
        document.getElementById('modifyUserName').value = data.name || '';
        document.getElementById('modifyUserEmail').value = data.email || '';
        document.getElementById('modifyUserContactNumber').value = data.contactNumber || '';
        document.getElementById('modifyUserState').value = data.state || '';
        
        // Show the form container
        document.getElementById('modify-user-form-container').classList.remove('d-none');
        
        showAlert('success', 'User found! You can now modify their details.');
    })
    .catch(error => {
        console.error('Error fetching user:', error);
        showAlert('danger', 'Error fetching user: ' + error.message);
        
        // Hide the form container
        document.getElementById('modify-user-form-container').classList.add('d-none');
    });
}

/**
 * Handle modify user form submission
 */
function handleModifyUser(e) {
    e.preventDefault();
    
    const userId = document.getElementById('modifyUserId').value;
    
    // Build request body with only non-empty fields
    const requestBody = {};
    
    const name = document.getElementById('modifyUserName').value.trim();
    if (name) {
        requestBody.name = name;
    }
    
    const email = document.getElementById('modifyUserEmail').value.trim();
    if (email) {
        requestBody.email = email;
    }
    
    const contactNumber = document.getElementById('modifyUserContactNumber').value.trim();
    if (contactNumber) {
        requestBody.contactNumber = contactNumber;
    }
    
    const state = document.getElementById('modifyUserState').value;
    if (state) {
        requestBody.state = state;
    }
    
    // Check if any field is populated
    if (Object.keys(requestBody).length === 0) {
        showAlert('warning', 'Please fill at least one field to update.');
        return;
    }
    
    // Send API request
    fetch(`/v1/users/${userId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to update user');
        }
        return response.json();
    })
    .then(data => {
        showAlert('success', 'User updated successfully!');
    })
    .catch(error => {
        console.error('Error updating user:', error);
        showAlert('danger', 'Error updating user: ' + error.message);
    });
}

/**
 * Handle lookup profile form submission
 */
function handleLookupProfile(e) {
    e.preventDefault();
    
    const profileId = document.getElementById('lookupProfileId').value;
    
    // Send API request to get profile details
    fetch(`/v1/profiles/${profileId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Profile not found');
        }
        return response.json();
    })
    .then(data => {
        // Store profile ID in hidden field
        document.getElementById('modifyProfileId').value = profileId;
        
        // Populate form with profile data
        document.getElementById('modifyDisplayName').value = data.displayName || '';
        document.getElementById('modifyAvatarUrl').value = data.avatarUrl || '';
        document.getElementById('modifyBiography').value = data.biography || '';
        document.getElementById('modifyJobTitle').value = data.jobTitle || '';
        document.getElementById('modifyDepartment').value = data.department || '';
        document.getElementById('modifyLocation').value = data.location || '';
        document.getElementById('modifyLinkedInUrl').value = data.linkedInUrl || '';
        document.getElementById('modifyTwitterUrl').value = data.twitterUrl || '';
        document.getElementById('modifyGitHubUrl').value = data.gitHubUrl || '';
        
        // Show the form container
        document.getElementById('modify-profile-form-container').classList.remove('d-none');
        
        showAlert('success', 'Profile found! You can now modify its details.');
    })
    .catch(error => {
        console.error('Error fetching profile:', error);
        showAlert('danger', 'Error fetching profile: ' + error.message);
        
        // Hide the form container
        document.getElementById('modify-profile-form-container').classList.add('d-none');
    });
}

/**
 * Handle modify profile form submission
 */
function handleModifyProfile(e) {
    e.preventDefault();
    
    const profileId = document.getElementById('modifyProfileId').value;
    
    // Build request body with only non-empty fields
    const requestBody = {};
    
    const displayName = document.getElementById('modifyDisplayName').value.trim();
    if (displayName) {
        requestBody.displayName = displayName;
    }
    
    const avatarUrl = document.getElementById('modifyAvatarUrl').value.trim();
    if (avatarUrl) {
        requestBody.avatarUrl = avatarUrl;
    }
    
    const biography = document.getElementById('modifyBiography').value.trim();
    if (biography) {
        requestBody.biography = biography;
    }
    
    const jobTitle = document.getElementById('modifyJobTitle').value.trim();
    if (jobTitle) {
        requestBody.jobTitle = jobTitle;
    }
    
    const department = document.getElementById('modifyDepartment').value.trim();
    if (department) {
        requestBody.department = department;
    }
    
    const location = document.getElementById('modifyLocation').value.trim();
    if (location) {
        requestBody.location = location;
    }
    
    const linkedInUrl = document.getElementById('modifyLinkedInUrl').value.trim();
    if (linkedInUrl) {
        requestBody.linkedInUrl = linkedInUrl;
    }
    
    const twitterUrl = document.getElementById('modifyTwitterUrl').value.trim();
    if (twitterUrl) {
        requestBody.twitterUrl = twitterUrl;
    }
    
    const gitHubUrl = document.getElementById('modifyGitHubUrl').value.trim();
    if (gitHubUrl) {
        requestBody.gitHubUrl = gitHubUrl;
    }
    
    // Check if any field is populated
    if (Object.keys(requestBody).length === 0) {
        showAlert('warning', 'Please fill at least one field to update.');
        return;
    }
    
    // Send API request
    fetch(`/v1/profiles/${profileId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to update profile');
        }
        return response.json();
    })
    .then(data => {
        showAlert('success', 'Profile updated successfully!');
    })
    .catch(error => {
        console.error('Error updating profile:', error);
        showAlert('danger', 'Error updating profile: ' + error.message);
    });
}

/**
 * Display profile details in the UI
 */
function displayProfileDetails(profile) {
    const profileDetails = document.getElementById('profile-details');
    const profileData = document.getElementById('profile-data');
    
    // Show the details container
    profileDetails.classList.remove('d-none');
    
    // Format the profile details
    profileData.innerHTML = `
        <dl class="row">
            <dt class="col-sm-3">Profile ID</dt>
            <dd class="col-sm-9">${profile.id}</dd>
            
            <dt class="col-sm-3">User ID</dt>
            <dd class="col-sm-9">${profile.userId}</dd>
            
            <dt class="col-sm-3">Tenant ID</dt>
            <dd class="col-sm-9">${profile.tenantId}</dd>
            
            <dt class="col-sm-3">Display Name</dt>
            <dd class="col-sm-9">${profile.displayName}</dd>
            
            <dt class="col-sm-3">Avatar URL</dt>
            <dd class="col-sm-9">${profile.avatarUrl || 'N/A'}</dd>
            
            <dt class="col-sm-3">Biography</dt>
            <dd class="col-sm-9">${profile.biography || 'N/A'}</dd>
            
            <dt class="col-sm-3">Job Title</dt>
            <dd class="col-sm-9">${profile.jobTitle || 'N/A'}</dd>
            
            <dt class="col-sm-3">Department</dt>
            <dd class="col-sm-9">${profile.department || 'N/A'}</dd>
            
            <dt class="col-sm-3">Location</dt>
            <dd class="col-sm-9">${profile.location || 'N/A'}</dd>
            
            <dt class="col-sm-3">LinkedIn URL</dt>
            <dd class="col-sm-9">${profile.linkedInUrl || 'N/A'}</dd>
            
            <dt class="col-sm-3">Twitter URL</dt>
            <dd class="col-sm-9">${profile.twitterUrl || 'N/A'}</dd>
            
            <dt class="col-sm-3">GitHub URL</dt>
            <dd class="col-sm-9">${profile.gitHubUrl || 'N/A'}</dd>
        </dl>
    `;
}

/**
 * Display list of profiles in the UI
 */
function displayProfilesList(profiles) {
    const profilesTable = document.getElementById('profiles-table');
    const profilesList = document.getElementById('profiles-list');
    const noProfilesMessage = document.getElementById('no-profiles-message');
    
    // Clear existing content
    profilesList.innerHTML = '';
    
    if (profiles && profiles.length > 0) {
        // Show the table and hide the no profiles message
        profilesTable.classList.remove('d-none');
        noProfilesMessage.classList.add('d-none');
        
        // Add each profile to the table
        profiles.forEach(profile => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${profile.id}</td>
                <td>${profile.userId}</td>
                <td>${profile.displayName}</td>
                <td>${profile.jobTitle || 'N/A'}</td>
                <td>
                    <button class="btn btn-sm btn-outline-primary view-profile" data-id="${profile.id}">View</button>
                </td>
            `;
            profilesList.appendChild(row);
        });
        
        // Add event listeners to view buttons
        document.querySelectorAll('.view-profile').forEach(button => {
            button.addEventListener('click', function() {
                const profileId = this.getAttribute('data-id');
                document.getElementById('profileId').value = profileId;
                
                // Switch to the view profile page
                showPage('view-profile');
                document.getElementById('view-profile-form').dispatchEvent(new Event('submit'));
            });
        });
    } else {
        // Hide the table and show the no profiles message
        profilesTable.classList.add('d-none');
        noProfilesMessage.classList.remove('d-none');
    }
}

/**
 * Show an alert message
 */
function showAlert(type, message) {
    const alertContainer = document.getElementById('alert-container');
    
    // Create the alert element
    const alertElement = document.createElement('div');
    alertElement.className = `alert alert-${type} alert-dismissible fade show`;
    alertElement.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;
    
    // Add the alert to the container
    alertContainer.appendChild(alertElement);
    
    // Auto-dismiss after 5 seconds
    setTimeout(() => {
        const dismissButton = alertElement.querySelector('.btn-close');
        if (dismissButton) {
            dismissButton.click();
        }
    }, 5000);
}

/**
 * Generate a random UUID v4
 */
function generateUUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        const r = Math.random() * 16 | 0;
        const v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

/**
 * Initialize all UUID generator buttons
 */
function initUuidGenerators() {
    // Find all UUID generation buttons
    const uuidButtons = document.querySelectorAll('.generate-uuid');
    
    // Add click event listeners to each button
    uuidButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Get the target input ID from the button's data attribute
            const targetInputId = this.getAttribute('data-target');
            const targetInput = document.getElementById(targetInputId);
            
            if (targetInput) {
                // Generate a new UUID and set it in the target input
                targetInput.value = generateUUID();
                
                // Add a visual indication that the UUID was generated
                targetInput.classList.add('bg-light');
                setTimeout(() => {
                    targetInput.classList.remove('bg-light');
                }, 300);
            }
        });
    });
}
