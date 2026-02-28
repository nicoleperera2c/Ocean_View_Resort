<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.oceanview.model.User" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>User Form - Ocean View Resort</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
            <style>
                .password-section {
                    margin-top: 30px;
                    padding-top: 20px;
                    border-top: 2px solid #e0e0e0;
                }

                .password-section h3 {
                    margin-bottom: 15px;
                    color: #333;
                }

                .validation-error {
                    color: #d9534f;
                    font-size: 12px;
                    margin-top: 4px;
                    display: none;
                }

                .form-group input:invalid {
                    border-color: #d9534f;
                }

                .password-strength {
                    font-size: 12px;
                    margin-top: 4px;
                }

                .strength-weak {
                    color: #d9534f;
                }

                .strength-medium {
                    color: #f0ad4e;
                }

                .strength-strong {
                    color: #5cb85c;
                }
            </style>
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container form-container">
                <% String action=(String) request.getAttribute("action"); boolean isEdit="edit" .equals(action); User
                    targetUser=isEdit ? (User) request.getAttribute("targetUser") : new User(); String formAction=isEdit
                    ? "edit" : "add" ; String usernameVal=targetUser.getUsername() !=null ? targetUser.getUsername()
                    : "" ; String fullNameVal=targetUser.getFullName() !=null ? targetUser.getFullName() : "" ; String
                    emailVal=targetUser.getEmail() !=null ? targetUser.getEmail() : "" ; String
                    phoneVal=targetUser.getPhone() !=null ? targetUser.getPhone() : "" ; String
                    adminSelected=targetUser.getRole()==User.UserRole.ADMIN ? "selected" : "" ; String
                    managerSelected=targetUser.getRole()==User.UserRole.MANAGER ? "selected" : "" ; String
                    staffSelected=targetUser.getRole()==User.UserRole.STAFF ? "selected" : "" ; String pageTitle=isEdit
                    ? "Edit User" : "Add New User" ; %>

                    <h2>
                        <%= pageTitle %>
                    </h2>

                    <% String formError=(String) request.getAttribute("errorMsg"); if (formError !=null) { %>
                        <div class="alert alert-danger">
                            <%= formError %>
                        </div>
                        <% } %>

                            <% String paramError=request.getParameter("error"); if (paramError !=null) { %>
                                <div class="alert alert-danger">
                                    <%= paramError %>
                                </div>
                                <% } %>

                                    <div class="card">
                                        <form action="<%=request.getContextPath()%>/app/users/<%= formAction %>"
                                            method="POST" class="standard-form" id="userForm"
                                            onsubmit="return validateForm();">

                                            <% if (isEdit) { %>
                                                <input type="hidden" name="userId"
                                                    value="<%= targetUser.getUserId() %>">
                                                <% } %>

                                                    <div class="form-group">
                                                        <label for="username">Username <% if (!isEdit) { %><span
                                                                    class="required">*</span>
                                                                <% } %></label>
                                                        <% if (isEdit) { %>
                                                            <input type="text" id="username" name="username"
                                                                value="<%= usernameVal %>" readonly
                                                                style="background-color:#f4f4f4; cursor:not-allowed;">
                                                            <small>Usernames cannot be changed after creation.</small>
                                                            <% } else { %>
                                                                <input type="text" id="username" name="username"
                                                                    value="<%= usernameVal %>" required minlength="3"
                                                                    placeholder="e.g. jdoe">
                                                                <div class="validation-error" id="usernameError">
                                                                    Username must be at least 3 characters.</div>
                                                                <% } %>
                                                    </div>

                                                    <% if (!isEdit) { %>
                                                        <div class="form-group">
                                                            <label for="password">Password <span
                                                                    class="required">*</span></label>
                                                            <input type="password" id="password" name="password"
                                                                required minlength="6" placeholder="Min 6 characters"
                                                                oninput="checkPasswordStrength(this.value);">
                                                            <div class="password-strength" id="passwordStrength"></div>
                                                            <div class="validation-error" id="passwordError">Password
                                                                must be at least 6 characters.</div>
                                                        </div>
                                                        <% } %>

                                                            <div class="form-group">
                                                                <label for="fullName">Full Name <span
                                                                        class="required">*</span></label>
                                                                <input type="text" id="fullName" name="fullName"
                                                                    value="<%= fullNameVal %>" required>
                                                                <div class="validation-error" id="fullNameError">Full
                                                                    Name is required.</div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="email">Email</label>
                                                                <input type="email" id="email" name="email"
                                                                    value="<%= emailVal %>"
                                                                    placeholder="name@example.com">
                                                                <div class="validation-error" id="emailError">Please
                                                                    enter a valid email address.</div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="phone">Phone</label>
                                                                <input type="tel" id="phone" name="phone"
                                                                    value="<%= phoneVal %>"
                                                                    placeholder="e.g. 0771234567">
                                                                <div class="validation-error" id="phoneError">Please
                                                                    enter a valid phone number.</div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="role">System Role <span
                                                                        class="required">*</span></label>
                                                                <select id="role" name="role" required>
                                                                    <option value="">-- Select a Role --</option>
                                                                    <option value="ADMIN" <%=adminSelected %>
                                                                        >Administrator</option>
                                                                    <option value="MANAGER" <%=managerSelected %>
                                                                        >Manager</option>
                                                                    <option value="STAFF" <%=staffSelected %>>Staff
                                                                    </option>
                                                                </select>
                                                                <div class="validation-error" id="roleError">Please
                                                                    select a role.</div>
                                                            </div>

                                                            <div class="form-actions">
                                                                <button type="submit" class="btn btn-primary">
                                                                    <%= isEdit ? "Update User" : "Create User" %>
                                                                </button>
                                                                <a href="<%=request.getContextPath()%>/app/users"
                                                                    class="btn btn-secondary">Cancel</a>
                                                            </div>

                                                            <% if (!isEdit) { %>
                                                                <small
                                                                    style="display:block; margin-top:15px; color:#666;">Note:
                                                                    New users are active by default and can log in
                                                                    immediately.</small>
                                                                <% } %>
                                        </form>

                                        <%-- Password Reset Section (only visible when editing) --%>
                                            <% if (isEdit) { %>
                                                <div class="password-section">
                                                    <h3>Reset Password</h3>
                                                    <form
                                                        action="<%=request.getContextPath()%>/app/users/reset-password"
                                                        method="POST" class="standard-form" id="resetPasswordForm"
                                                        onsubmit="return validateResetPassword();">
                                                        <input type="hidden" name="userId"
                                                            value="<%= targetUser.getUserId() %>">

                                                        <div class="form-group">
                                                            <label for="newPassword">New Password <span
                                                                    class="required">*</span></label>
                                                            <input type="password" id="newPassword" name="newPassword"
                                                                required minlength="6" placeholder="Min 6 characters"
                                                                oninput="checkResetPasswordStrength(this.value);">
                                                            <div class="password-strength" id="resetPasswordStrength">
                                                            </div>
                                                            <div class="validation-error" id="newPasswordError">Password
                                                                must be at least 6 characters.</div>
                                                        </div>

                                                        <div class="form-group">
                                                            <label for="confirmPassword">Confirm Password <span
                                                                    class="required">*</span></label>
                                                            <input type="password" id="confirmPassword"
                                                                name="confirmPassword" required minlength="6"
                                                                placeholder="Re-enter new password">
                                                            <div class="validation-error" id="confirmPasswordError">
                                                                Passwords do not match.</div>
                                                        </div>

                                                        <div class="form-actions">
                                                            <button type="submit" class="btn btn-danger"
                                                                onclick="return confirm('Are you sure you want to reset this user\'s password?');">Reset
                                                                Password</button>
                                                        </div>
                                                    </form>
                                                </div>
                                                <% } %>
                                    </div>
            </main>

            <script>
                // --- Client-Side Validation ---
                function validateForm() {
                    var isValid = true;
                    hideAllErrors();

                    var username = document.getElementById('username');
                    if (username && !username.readOnly && username.value.trim().length < 3) {
                        showError('usernameError');
                        isValid = false;
                    }

                    var password = document.getElementById('password');
                    if (password && password.value.length < 6) {
                        showError('passwordError');
                        isValid = false;
                    }

                    var fullName = document.getElementById('fullName');
                    if (fullName.value.trim().length === 0) {
                        showError('fullNameError');
                        isValid = false;
                    }

                    var email = document.getElementById('email');
                    if (email.value.trim().length > 0) {
                        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                        if (!emailRegex.test(email.value)) {
                            showError('emailError');
                            isValid = false;
                        }
                    }

                    var role = document.getElementById('role');
                    if (role.value === '') {
                        showError('roleError');
                        isValid = false;
                    }

                    return isValid;
                }

                function validateResetPassword() {
                    var newPwd = document.getElementById('newPassword').value;
                    var confirmPwd = document.getElementById('confirmPassword').value;
                    var isValid = true;

                    document.getElementById('newPasswordError').style.display = 'none';
                    document.getElementById('confirmPasswordError').style.display = 'none';

                    if (newPwd.length < 6) {
                        showError('newPasswordError');
                        isValid = false;
                    }

                    if (newPwd !== confirmPwd) {
                        showError('confirmPasswordError');
                        isValid = false;
                    }

                    return isValid;
                }

                function checkPasswordStrength(password) {
                    var el = document.getElementById('passwordStrength');
                    if (password.length === 0) { el.innerHTML = ''; return; }
                    if (password.length < 6) {
                        el.innerHTML = '<span class="strength-weak">Weak - Too short</span>';
                    } else if (password.length < 10) {
                        el.innerHTML = '<span class="strength-medium">Medium</span>';
                    } else {
                        el.innerHTML = '<span class="strength-strong">Strong</span>';
                    }
                }

                function checkResetPasswordStrength(password) {
                    var el = document.getElementById('resetPasswordStrength');
                    if (password.length === 0) { el.innerHTML = ''; return; }
                    if (password.length < 6) {
                        el.innerHTML = '<span class="strength-weak">Weak - Too short</span>';
                    } else if (password.length < 10) {
                        el.innerHTML = '<span class="strength-medium">Medium</span>';
                    } else {
                        el.innerHTML = '<span class="strength-strong">Strong</span>';
                    }
                }

                function showError(id) {
                    var el = document.getElementById(id);
                    if (el) el.style.display = 'block';
                }

                function hideAllErrors() {
                    var errors = document.querySelectorAll('.validation-error');
                    for (var i = 0; i < errors.length; i++) {
                        errors[i].style.display = 'none';
                    }
                }
            </script>
        </body>

        </html>