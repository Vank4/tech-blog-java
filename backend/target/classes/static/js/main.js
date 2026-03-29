(() => {
    const forms = document.querySelectorAll("[data-auth-form]");
    const url = new URL(window.location.href);
    const authStateEventName = "techblog:auth-changed";
    const postLoginNext = url.searchParams.get("next");

    const normalizeMessage = (message) => {
        if (!message) {
            return "";
        }

        const translations = {
            "Register successful. Please verify your email before login.": "Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản trước khi đăng nhập.",
            "Login successful": "Đăng nhập thành công",
            "Email is not verified": "Email của bạn chưa được xác thực. Vui lòng kiểm tra hộp thư và xác thực tài khoản trước khi đăng nhập.",
            "Invalid email or password": "Email hoặc mật khẩu không đúng",
            "Email already exists": "Email này đã được sử dụng",
            "Validation failed": "Dữ liệu nhập chưa hợp lệ",
            "Get profile successful": "Tải hồ sơ thành công",
            "Update profile successful": "Cập nhật hồ sơ thành công",
            "Change password successful": "Đổi mật khẩu thành công",
            "Users retrieved successfully": "Tải danh sách người dùng thành công",
            "User roles updated successfully": "Cập nhật quyền thành công",
            "User banned successfully": "Khóa tài khoản thành công",
            "User unbanned successfully": "Mở khóa tài khoản thành công",
            "User not found": "Không tìm thấy người dùng"
        };

        return translations[message] || message;
    };

    const formatDate = (value) => {
        if (!value) {
            return "Chưa có dữ liệu";
        }

        const date = new Date(value);
        if (Number.isNaN(date.getTime())) {
            return value;
        }

        return new Intl.DateTimeFormat("vi-VN", {
            dateStyle: "medium",
            timeStyle: "short"
        }).format(date);
    };

    const getToken = () => localStorage.getItem("techblog.accessToken");
    const emitAuthStateChange = () => {
        window.dispatchEvent(new CustomEvent(authStateEventName));
    };

    const buildAvatarDataUri = (label = "U") => {
        const safeLabel = encodeURIComponent((label || "U").trim().charAt(0).toUpperCase() || "U");
        return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(
            `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 80 80">
                <rect width="80" height="80" rx="40" fill="#0f172a"/>
                <rect x="2" y="2" width="76" height="76" rx="38" fill="none" stroke="#22d3ee" stroke-opacity="0.22"/>
                <text x="40" y="48" text-anchor="middle" font-size="30" font-family="Inter,Arial,sans-serif" font-weight="700" fill="#e2f8fb">${decodeURIComponent(safeLabel)}</text>
            </svg>`
        )}`;
    };

    const resolveAvatarUrl = (avatarUrl, fallbackLabel = "U") => {
        if (!avatarUrl) {
            return buildAvatarDataUri(fallbackLabel);
        }

        if (avatarUrl.startsWith("http://") || avatarUrl.startsWith("https://") || avatarUrl.startsWith("data:")) {
            return avatarUrl;
        }

        if (avatarUrl.startsWith("/")) {
            return avatarUrl;
        }

        return `/${avatarUrl}`;
    };

    const setElementVisible = (element, shouldShow, displayValue = "inline-flex") => {
        if (!element) {
            return;
        }

        element.style.display = shouldShow ? displayValue : "none";
    };

    const getRoles = (profile) => {
        const roles = profile?.roles || [];
        return Array.isArray(roles) ? roles : Array.from(roles);
    };

    const getSafePostLoginNext = () => {
        if (!postLoginNext || !postLoginNext.startsWith("/")) {
            return null;
        }

        return postLoginNext;
    };

    const updateActiveNavigation = () => {
        const pathname = window.location.pathname || "/";
        document.querySelectorAll("[data-nav-link]").forEach((link) => {
            const target = link.dataset.navLink || "";
            const exact = link.dataset.navExact === "true";
            const isActive = exact ? pathname === target : pathname === target || pathname.startsWith(`${target}/`);
            link.classList.toggle("active", isActive);
        });
    };

    const applyPublicHeaderState = (profile) => {
        updateActiveNavigation();

        const roles = getRoles(profile);
        const isAuthenticated = Boolean(profile);
        const guestPanel = document.querySelector("[data-guest-panel]");
        const authPanel = document.querySelector("[data-auth-panel]");
        const avatarLink = document.querySelector("[data-avatar-link]");
        const avatar = document.querySelector("[data-user-avatar]");
        const userName = document.querySelector("[data-user-name]");
        const userCaption = document.querySelector("[data-user-caption]");
        const profileLink = document.querySelector("[data-nav-profile]");
        const authorLink = document.querySelector("[data-nav-author]");
        const adminLink = document.querySelector("[data-nav-admin]");

        setElementVisible(guestPanel, !isAuthenticated, "flex");
        setElementVisible(authPanel, isAuthenticated, "flex");
        setElementVisible(profileLink, isAuthenticated, "flex");
        setElementVisible(authorLink, roles.includes("AUTHOR") || roles.includes("ADMIN"), "flex");
        setElementVisible(adminLink, roles.includes("ADMIN"), "flex");

        if (!isAuthenticated) {
            if (avatarLink) {
                avatarLink.href = "/login";
            }
            if (avatar) {
                avatar.src = buildAvatarDataUri("U");
                avatar.alt = "Guest account";
            }
            if (userName) {
                userName.textContent = "Guest";
            }
            if (userCaption) {
                userCaption.textContent = "Sign in";
            }
            return;
        }

        const displayName = profile.fullName || profile.email || "TechNexus User";
        const primaryRole = roles.includes("ADMIN") ? "Admin" : roles.includes("AUTHOR") ? "Author" : "Member";

        if (avatarLink) {
            avatarLink.href = "/profile";
        }
        if (avatar) {
            avatar.src = resolveAvatarUrl(profile.avatarUrl, displayName);
            avatar.alt = `${displayName} avatar`;
        }
        if (userName) {
            userName.textContent = displayName;
        }
        if (userCaption) {
            userCaption.textContent = `${primaryRole} account`;
        }
    };

    const loadCurrentUserState = async () => {
        const token = getToken();
        if (!token) {
            applyPublicHeaderState(null);
            return null;
        }

        try {
            const response = await fetch("/api/v1/users/me", {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            const data = await response.json().catch(() => ({}));
            if (!response.ok || data.success === false) {
                localStorage.removeItem("techblog.accessToken");
                applyPublicHeaderState(null);
                return null;
            }

            applyPublicHeaderState(data.data || null);
            return data.data || null;
        } catch (error) {
            applyPublicHeaderState(null);
            return null;
        }
    };

    const fetchCurrentProfile = async () => {
        const { response, data } = await authFetch("/api/v1/users/me");
        return {
            response,
            data,
            profile: data.data || {},
            roles: data.data?.roles || []
        };
    };

    const getPostLoginRedirect = (roles = []) => {
        const next = getSafePostLoginNext();
        if (next) {
            return next;
        }

        if (roles.includes("ADMIN")) {
            return "/admin/posts";
        }

        if (roles.includes("AUTHOR")) {
            return "/author/posts";
        }

        return "/profile";
    };

    const authFetch = async (input, init = {}) => {
        const token = getToken();
        const headers = new Headers(init.headers || {});

        if (!headers.has("Content-Type") && init.body && !(init.body instanceof FormData)) {
            headers.set("Content-Type", "application/json");
        }

        if (token) {
            headers.set("Authorization", `Bearer ${token}`);
        }

        const response = await fetch(input, {
            ...init,
            headers
        });

        const data = await response.json().catch(() => ({}));

        if (response.status === 401) {
            localStorage.removeItem("techblog.accessToken");
            emitAuthStateChange();
            const next = encodeURIComponent(`${window.location.pathname}${window.location.search}`);
            window.location.href = `/login?unauthorized=1&next=${next}`;
            throw new Error("UNAUTHORIZED");
        }

        return { response, data };
    };

    const setMessage = (box, kind, message) => {
        if (!box) {
            return;
        }

        box.textContent = normalizeMessage(message);
        box.className = `status-message is-visible is-${kind}`;
    };

    const clearMessage = (box) => {
        if (!box) {
            return;
        }

        box.textContent = "";
        box.className = "status-message";
    };

    const setFormMessage = (form, kind, message) => {
        setMessage(form.querySelector("[data-form-message]"), kind, message);
    };

    const clearFormMessage = (form) => {
        clearMessage(form.querySelector("[data-form-message]"));
    };

    const setLoading = (form, isLoading) => {
        const button = form.querySelector("button[type='submit']");
        if (!button) {
            return;
        }

        button.disabled = isLoading;
        button.textContent = isLoading
            ? button.dataset.loadingLabel || "Đang xử lý..."
            : button.dataset.defaultLabel || "Gửi";
    };

    const roleBadge = (role) => `
        <span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-primary/10 text-primary border border-primary/20">
            ${role}
        </span>
    `;
    const statusBadge = (status) => {
        const key = (status || "").toUpperCase();
        const map = {
            ACTIVE: "bg-emerald-500/10 text-emerald-400 border-emerald-500/20",
            BANNED: "bg-rose-500/10 text-rose-300 border-rose-500/20",
            INACTIVE: "bg-amber-500/10 text-amber-300 border-amber-500/20"
        };
        return `<span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold border ${map[key] || "bg-slate-800 text-slate-300 border-slate-700"}">${key || "UNKNOWN"}</span>`;
    };

    const setupAuthForms = () => {
        if (!forms.length) {
            return;
        }

        forms.forEach((form) => {
            form.addEventListener("submit", async (event) => {
                event.preventDefault();
                clearFormMessage(form);

                const formType = form.dataset.authForm;
                const formData = new FormData(form);

                if (formType === "register" && formData.get("password") !== formData.get("confirmPassword")) {
                    setFormMessage(form, "error", "Mật khẩu xác nhận chưa khớp.");
                    return;
                }

                const payload = Object.fromEntries(formData.entries());
                const endpoint = form.dataset.endpoint;

                if (!endpoint) {
                    setFormMessage(form, "error", "Thiếu cấu hình endpoint cho biểu mẫu.");
                    return;
                }

                if (formType === "register") {
                    delete payload.confirmPassword;
                    payload.fullName = payload.fullName?.trim();
                }

                setLoading(form, true);

                try {
                    const response = await fetch(endpoint, {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify(payload)
                    });

                    const data = await response.json().catch(() => ({}));

                    if (!response.ok || data.success === false) {
                        const errorMessage = data.message || "Yêu cầu chưa thành công. Vui lòng thử lại.";
                        setFormMessage(form, "error", errorMessage);
                        return;
                    }

                    if (formType === "login" && data.data?.accessToken) {
                        localStorage.setItem("techblog.accessToken", data.data.accessToken);
                        emitAuthStateChange();
                        setFormMessage(form, "success", "Đăng nhập thành công. Đang chuyển đến khu vực phù hợp...");
                        const roles = Array.isArray(data.data.roles) ? data.data.roles : Array.from(data.data.roles || []);
                        const target = getPostLoginRedirect(roles);
                        setTimeout(() => {
                            window.location.href = target;
                        }, 900);
                        return;
                    }

                    if (formType === "register") {
                        form.reset();
                        setFormMessage(form, "success", "Đăng ký thành công. Đang chuyển đến trang đăng nhập...");
                        setTimeout(() => {
                            window.location.href = "/login?registered=1";
                        }, 1200);
                        return;
                    }

                    form.reset();
                    setFormMessage(form, "success", data.message || "Thao tác thành công.");
                } catch (error) {
                    setFormMessage(form, "error", "Không thể kết nối đến máy chủ. Hãy kiểm tra backend rồi thử lại.");
                } finally {
                    setLoading(form, false);
                }
            });
        });

        const loginForm = document.querySelector('[data-auth-form="login"]');
        if (loginForm) {
            if (url.searchParams.get("registered") === "1") {
                setFormMessage(
                    loginForm,
                    "success",
                    "Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản trước khi đăng nhập."
                );
            }
            if (url.searchParams.get("unauthorized") === "1") {
                setFormMessage(
                    loginForm,
                    "error",
                    "Phiên đăng nhập không hợp lệ hoặc đã hết hạn. Vui lòng đăng nhập lại."
                );
            }

            ["registered", "unauthorized", "next"].forEach((key) => url.searchParams.delete(key));
            window.history.replaceState({}, "", `${url.pathname}${url.search}`);
        }
    };

    const setupLogoutButtons = () => {
        document.querySelectorAll("[data-logout]").forEach((button) => {
            if (button.dataset.logoutBound === "true") {
                return;
            }

            button.dataset.logoutBound = "true";
            button.addEventListener("click", () => {
                localStorage.removeItem("techblog.accessToken");
                emitAuthStateChange();

                if (button.dataset.logoutRedirect) {
                    window.location.href = button.dataset.logoutRedirect;
                    return;
                }

                if (window.location.pathname.startsWith("/admin")
                        || window.location.pathname.startsWith("/author")
                        || window.location.pathname === "/profile") {
                    window.location.href = "/";
                    return;
                }

                window.location.href = `${window.location.pathname}${window.location.search}`;
            });
        });
    };

    const setupProfilePage = async () => {
        const root = document.querySelector("[data-page='profile']");
        if (!root) {
            return;
        }

        if (root.dataset.profileInline === "true") {
            return;
        }

        if (!getToken()) {
            window.location.href = "/login?next=%2Fprofile";
            return;
        }

        const statusBox = root.querySelector("[data-profile-status]");
        const profileForm = root.querySelector("[data-profile-form]");
        const passwordForm = root.querySelector("[data-password-form]");
        const summaryName = root.querySelector("[data-profile-name]");
        const summaryEmail = root.querySelector("[data-profile-email]");
        const summaryBio = root.querySelector("[data-profile-bio]");
        const summaryRoles = root.querySelector("[data-profile-roles]");
        const summaryStatus = root.querySelector("[data-profile-status-badge]");
        const summaryVerified = root.querySelector("[data-profile-verified]");
        const avatar = root.querySelector("[data-profile-avatar]");
        const avatarFallback = root.querySelector("[data-profile-avatar-fallback]");
        const countRoles = root.querySelector("[data-stat-roles]");
        const countVerified = root.querySelector("[data-stat-verified]");
        const countStatus = root.querySelector("[data-stat-status]");

        const loadProfile = async () => {
            clearMessage(statusBox);
            try {
                const { response, data } = await authFetch("/api/v1/users/me");
                if (!response.ok || data.success === false) {
                    setMessage(statusBox, "error", data.message || "Không thể tải hồ sơ.");
                    return;
                }

                const profile = data.data || {};
                summaryName.textContent = profile.fullName || "Người dùng TechNexus";
                summaryEmail.textContent = profile.email || "Chưa có email";
                summaryBio.textContent = profile.bio || "Chưa có mô tả cá nhân. Hãy thêm bio để hoàn thiện hồ sơ của bạn.";
                summaryRoles.innerHTML = (profile.roles || []).map(roleBadge).join("") || '<span class="badge">Chưa có role</span>';
                summaryStatus.innerHTML = statusBadge(profile.status);
                summaryVerified.innerHTML = profile.emailVerified
                    ? '<span class="badge is-success">Đã xác thực email</span>'
                    : '<span class="badge is-warning">Chưa xác thực email</span>';
                const avatarText = (profile.fullName || profile.email || "U").trim().charAt(0).toUpperCase();
                if (avatarFallback) {
                    avatarFallback.textContent = avatarText;
                } else if (avatar) {
                    avatar.textContent = avatarText;
                }

                profileForm.querySelector('[name="fullName"]').value = profile.fullName || "";
                profileForm.querySelector('[name="avatarUrl"]').value = profile.avatarUrl || "";
                profileForm.querySelector('[name="bio"]').value = profile.bio || "";

                if (countRoles) {
                    countRoles.textContent = `${(profile.roles || []).length}`;
                }
                if (countVerified) {
                    countVerified.textContent = profile.emailVerified ? "Yes" : "No";
                }
                if (countStatus) {
                    countStatus.textContent = profile.status || "UNKNOWN";
                }

                const authorLink = root.querySelector("[data-author-link]");
                const adminPostsLink = root.querySelector("[data-admin-posts-link]");
                const adminUsersLink = root.querySelector("[data-admin-users-link]");
                const roles = profile.roles || [];

                if (authorLink) {
                    authorLink.style.display = roles.includes("AUTHOR") || roles.includes("ADMIN") ? "inline-flex" : "none";
                }

                if (adminPostsLink) {
                    adminPostsLink.style.display = roles.includes("ADMIN") ? "inline-flex" : "none";
                }

                if (adminUsersLink) {
                    adminUsersLink.style.display = roles.includes("ADMIN") ? "inline-flex" : "none";
                }
            } catch (error) {
                if (error.message !== "UNAUTHORIZED") {
                    setMessage(statusBox, "error", "Không thể tải hồ sơ từ máy chủ.");
                }
            }
        };

        profileForm?.addEventListener("submit", async (event) => {
            event.preventDefault();
            clearFormMessage(profileForm);
            setLoading(profileForm, true);

            const payload = {
                fullName: profileForm.querySelector('[name="fullName"]').value.trim(),
                avatarUrl: profileForm.querySelector('[name="avatarUrl"]').value.trim(),
                bio: profileForm.querySelector('[name="bio"]').value.trim()
            };

            try {
                const { response, data } = await authFetch("/api/v1/users/me", {
                    method: "PUT",
                    body: JSON.stringify(payload)
                });

                if (!response.ok || data.success === false) {
                    setFormMessage(profileForm, "error", data.message || "Không thể cập nhật hồ sơ.");
                    return;
                }

                setFormMessage(profileForm, "success", data.message || "Cập nhật hồ sơ thành công.");
                await loadProfile();
            } catch (error) {
                if (error.message !== "UNAUTHORIZED") {
                    setFormMessage(profileForm, "error", "Không thể cập nhật hồ sơ lúc này.");
                }
            } finally {
                setLoading(profileForm, false);
            }
        });

        passwordForm?.addEventListener("submit", async (event) => {
            event.preventDefault();
            clearFormMessage(passwordForm);

            const currentPassword = passwordForm.querySelector('[name="currentPassword"]').value;
            const newPassword = passwordForm.querySelector('[name="newPassword"]').value;
            const confirmPassword = passwordForm.querySelector('[name="confirmPassword"]').value;

            if (newPassword !== confirmPassword) {
                setFormMessage(passwordForm, "error", "Mật khẩu mới và xác nhận mật khẩu chưa khớp.");
                return;
            }

            setLoading(passwordForm, true);

            try {
                const { response, data } = await authFetch("/api/v1/users/change-password", {
                    method: "POST",
                    body: JSON.stringify({ currentPassword, newPassword })
                });

                if (!response.ok || data.success === false) {
                    setFormMessage(passwordForm, "error", data.message || "Không thể đổi mật khẩu.");
                    return;
                }

                passwordForm.reset();
                setFormMessage(passwordForm, "success", data.message || "Đổi mật khẩu thành công.");
            } catch (error) {
                if (error.message !== "UNAUTHORIZED") {
                    setFormMessage(passwordForm, "error", "Không thể đổi mật khẩu lúc này.");
                }
            } finally {
                setLoading(passwordForm, false);
            }
        });

        await loadProfile();
    };

    const setupForgotPasswordPage = () => {
        const root = document.querySelector("[data-page='forgot-password']");
        if (!root) {
            return;
        }

        const form = root.querySelector("[data-forgot-password-form]");
        if (!form) {
            return;
        }

        form.addEventListener("submit", async (event) => {
            event.preventDefault();
            clearFormMessage(form);
            setLoading(form, true);

            const email = form.querySelector('[name="email"]').value.trim();

            try {
                const response = await fetch("/api/v1/auth/forgot-password", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ email })
                });

                const data = await response.json().catch(() => ({}));

                if (!response.ok || data.success === false) {
                    setFormMessage(form, "error", data.message || "Không thể gửi email đặt lại mật khẩu.");
                    return;
                }

                form.reset();
                setFormMessage(form, "success", data.message || "Đã gửi liên kết đặt lại mật khẩu.");
            } catch (error) {
                setFormMessage(form, "error", "Không thể kết nối đến máy chủ. Hãy thử lại sau.");
            } finally {
                setLoading(form, false);
            }
        });
    };

    const setupResetPasswordPage = async () => {
        const root = document.querySelector("[data-page='reset-password']");
        if (!root) {
            return;
        }

        const form = root.querySelector("[data-reset-password-form]");
        const tokenHint = root.querySelector("[data-token-hint]");
        if (!form) {
            return;
        }

        const token = new URL(window.location.href).searchParams.get("token");
        if (!token) {
            setFormMessage(form, "error", "Thiếu token đặt lại mật khẩu.");
            if (tokenHint) {
                tokenHint.textContent = "Liên kết không hợp lệ hoặc bị thiếu token.";
            }
            form.querySelector("button[type='submit']").disabled = true;
            return;
        }

        try {
            const response = await fetch(`/api/v1/auth/validate-reset-token?token=${encodeURIComponent(token)}`);
            const data = await response.json().catch(() => ({}));

            if (!response.ok || data.success === false) {
                setFormMessage(form, "error", data.message || "Liên kết đặt lại mật khẩu không hợp lệ.");
                if (tokenHint) {
                    tokenHint.textContent = "Token đã hết hạn, không hợp lệ hoặc đã được sử dụng.";
                }
                form.querySelector("button[type='submit']").disabled = true;
                return;
            }

            if (tokenHint) {
                tokenHint.textContent = "Token hợp lệ. Bạn có thể nhập mật khẩu mới bên dưới.";
            }
        } catch (error) {
            setFormMessage(form, "error", "Không thể xác thực token đặt lại mật khẩu.");
            form.querySelector("button[type='submit']").disabled = true;
            return;
        }

        form.addEventListener("submit", async (event) => {
            event.preventDefault();
            clearFormMessage(form);

            const newPassword = form.querySelector('[name="newPassword"]').value;
            const confirmPassword = form.querySelector('[name="confirmPassword"]').value;

            if (newPassword !== confirmPassword) {
                setFormMessage(form, "error", "Mật khẩu xác nhận chưa khớp.");
                return;
            }

            setLoading(form, true);

            try {
                const response = await fetch("/api/v1/auth/reset-password", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ token, newPassword })
                });

                const data = await response.json().catch(() => ({}));

                if (!response.ok || data.success === false) {
                    setFormMessage(form, "error", data.message || "Không thể đặt lại mật khẩu.");
                    return;
                }

                form.reset();
                setFormMessage(form, "success", data.message || "Đặt lại mật khẩu thành công. Đang chuyển đến đăng nhập...");
                setTimeout(() => {
                    window.location.href = "/login";
                }, 1200);
            } catch (error) {
                setFormMessage(form, "error", "Không thể kết nối đến máy chủ. Hãy thử lại sau.");
            } finally {
                setLoading(form, false);
            }
        });
    };
    const setupAdminUsersPage = async () => {
        const root = document.querySelector("[data-page='admin-users']");
        if (!root) {
            return;
        }

        if (!getToken()) {
            window.location.href = "/login?next=%2Fadmin%2Fusers";
            return;
        }

        const statusBox = root.querySelector("[data-admin-status]");
        const tableBody = root.querySelector("[data-user-table]");
        const detailName = root.querySelector("[data-detail-name]");
        const detailMeta = root.querySelector("[data-detail-meta]");
        const detailRoles = root.querySelector("[data-detail-roles]");
        const detailStatus = root.querySelector("[data-detail-status]");
        const detailCreated = root.querySelector("[data-detail-created]");
        const totalCount = root.querySelector("[data-total-count]");
        const pageCount = root.querySelector("[data-page-count]");
        const filterForm = root.querySelector("[data-filter-form]");
        const rolesForm = root.querySelector("[data-roles-form]");
        const banForm = root.querySelector("[data-ban-form]");
        const unbanButton = root.querySelector("[data-unban]");
        const prevButton = root.querySelector("[data-prev-page]");
        const nextButton = root.querySelector("[data-next-page]");

        let currentPage = 0;
        const currentSize = 8;
        let currentKeyword = "";
        let currentStatus = "";
        let currentUser = null;
        let totalElements = 0;

        try {
            const { response, roles } = await fetchCurrentProfile();
            if (!response.ok || !roles.includes("ADMIN")) {
                window.location.href = "/profile";
                return;
            }
        } catch (error) {
            if (error.message !== "UNAUTHORIZED") {
                window.location.href = "/profile";
            }
            return;
        }

        const renderDetails = (user) => {
            currentUser = user;
            detailName.textContent = user ? user.fullName || "Chưa có tên hiển thị" : "Chưa chọn người dùng";
            detailMeta.textContent = user ? user.email || "" : "Chọn một người dùng trong bảng để xem chi tiết và thao tác.";
            detailRoles.innerHTML = user
                ? (user.roles || []).map(roleBadge).join("") || '<span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-slate-800 text-slate-300 border border-slate-700">Không có role</span>'
                : '<span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-slate-800 text-slate-300 border border-slate-700">Chưa có dữ liệu</span>';
            detailStatus.innerHTML = user
                ? statusBadge(user.status)
                : '<span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-slate-800 text-slate-300 border border-slate-700">UNKNOWN</span>';
            detailCreated.textContent = user ? formatDate(user.createdAt) : "Chưa có dữ liệu";

            if (rolesForm) {
                rolesForm.querySelectorAll('input[name="roles"]').forEach((input) => {
                    input.checked = !!user && (user.roles || []).includes(input.value);
                });
            }

            if (unbanButton) {
                unbanButton.disabled = !user || user.status !== "BANNED";
            }
        };

        const renderTable = (users) => {
            if (!users.length) {
                tableBody.innerHTML = `
                    <tr>
                        <td colspan="6" class="px-6 py-10 text-center text-slate-500">
                            Không tìm thấy người dùng phù hợp với bộ lọc hiện tại.
                        </td>
                    </tr>
                `;
                return;
            }

            tableBody.innerHTML = users.map((user) => `
                <tr class="border-b border-slate-800/50 transition-colors ${currentUser && currentUser.id === user.id ? "bg-primary/10" : "hover:bg-slate-800/30"}">
                    <td class="px-6 py-4">
                        <div class="flex flex-col gap-1">
                            <strong class="text-white font-semibold">${user.fullName || "Chưa có tên"}</strong>
                            <span class="text-slate-400">${user.email || ""}</span>
                        </div>
                    </td>
                    <td class="px-6 py-4 text-center">${statusBadge(user.status)}</td>
                    <td class="px-6 py-4 text-center">${user.emailVerified ? '<span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">Đã xác thực</span>' : '<span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-amber-500/10 text-amber-300 border border-amber-500/20">Chưa xác thực</span>'}</td>
                    <td class="px-6 py-4"><div class="flex flex-wrap items-center justify-center gap-2">${(user.roles || []).map(roleBadge).join("")}</div></td>
                    <td class="px-6 py-4 text-slate-300">${formatDate(user.createdAt)}</td>
                    <td class="px-6 py-4 text-right">
                        <button class="inline-flex items-center justify-center px-4 py-2 rounded-xl border border-slate-700 text-slate-200 hover:text-white hover:border-primary/40 hover:bg-slate-800 transition-colors" type="button" data-select-user="${user.id}">
                            Chi tiết
                        </button>
                    </td>
                </tr>
            `).join("");

            tableBody.querySelectorAll("[data-select-user]").forEach((button) => {
                button.addEventListener("click", () => loadUserDetail(button.dataset.selectUser));
            });

            tableBody.querySelectorAll("tr").forEach((row) => {
                const button = row.querySelector("[data-select-user]");
                if (!button) {
                    return;
                }

                row.addEventListener("click", (event) => {
                    if (event.target.closest("button")) {
                        return;
                    }
                    loadUserDetail(button.dataset.selectUser);
                });
            });
        };

        const updatePager = () => {
            const totalPages = Math.max(1, Math.ceil(totalElements / currentSize));
            totalCount.textContent = `${totalElements}`;
            pageCount.textContent = `Trang ${currentPage + 1} / ${totalPages}`;
            prevButton.disabled = currentPage <= 0;
            nextButton.disabled = currentPage + 1 >= totalPages;
        };

        const loadUserDetail = async (id) => {
            clearMessage(statusBox);
            try {
                const { response, data } = await authFetch(`/api/v1/admin/users/${id}`);
                if (!response.ok || data.success === false) {
                    setMessage(statusBox, "error", data.message || "Không thể tải chi tiết người dùng.");
                    return;
                }

                renderDetails(data.data);
                await loadUsers();
            } catch (error) {
                if (error.message !== "UNAUTHORIZED") {
                    setMessage(statusBox, "error", "Không thể tải chi tiết người dùng.");
                }
            }
        };

        const loadUsers = async () => {
            clearMessage(statusBox);
            const params = new URLSearchParams({
                page: String(currentPage),
                size: String(currentSize)
            });

            if (currentKeyword) {
                params.set("keyword", currentKeyword);
            }

            if (currentStatus) {
                params.set("status", currentStatus);
            }

            try {
                const { response, data } = await authFetch(`/api/v1/admin/users?${params.toString()}`);
                if (response.status === 403) {
                    setMessage(statusBox, "error", "Bạn không có quyền truy cập khu vực quản trị người dùng.");
                    return;
                }

                if (!response.ok || data.success === false) {
                    setMessage(statusBox, "error", data.message || "Không thể tải danh sách người dùng.");
                    return;
                }

                const payload = data.data || { content: [], totalElements: 0 };
                totalElements = payload.totalElements || 0;
                renderTable(payload.content || []);
                updatePager();

                if (!currentUser && payload.content?.length) {
                    renderDetails(payload.content[0]);
                } else if (currentUser) {
                    const found = payload.content?.find((item) => item.id === currentUser.id);
                    if (found) {
                        renderDetails(found);
                    }
                }
            } catch (error) {
                if (error.message !== "UNAUTHORIZED") {
                    setMessage(statusBox, "error", "Không thể tải dữ liệu quản trị.");
                }
            }
        };

        filterForm?.addEventListener("submit", async (event) => {
            event.preventDefault();
            currentKeyword = filterForm.querySelector('[name="keyword"]').value.trim();
            currentStatus = filterForm.querySelector('[name="status"]').value;
            currentPage = 0;
            await loadUsers();
        });

        rolesForm?.addEventListener("submit", async (event) => {
            event.preventDefault();
            clearFormMessage(rolesForm);

            if (!currentUser) {
                setFormMessage(rolesForm, "error", "Hãy chọn một người dùng trước.");
                return;
            }

            const roles = [...rolesForm.querySelectorAll('input[name="roles"]:checked')].map((input) => input.value);
            if (!roles.length) {
                setFormMessage(rolesForm, "error", "Phải chọn ít nhất một role.");
                return;
            }

            setLoading(rolesForm, true);

            try {
                const { response, data } = await authFetch(`/api/v1/admin/users/${currentUser.id}/roles`, {
                    method: "PATCH",
                    body: JSON.stringify({ roles })
                });

                if (!response.ok || data.success === false) {
                    setFormMessage(rolesForm, "error", data.message || "Không thể cập nhật quyền.");
                    return;
                }

                setFormMessage(rolesForm, "success", data.message || "Cập nhật quyền thành công.");
                await loadUserDetail(currentUser.id);
            } catch (error) {
                if (error.message !== "UNAUTHORIZED") {
                    setFormMessage(rolesForm, "error", "Không thể cập nhật quyền lúc này.");
                }
            } finally {
                setLoading(rolesForm, false);
            }
        });

        banForm?.addEventListener("submit", async (event) => {
            event.preventDefault();
            clearFormMessage(banForm);

            if (!currentUser) {
                setFormMessage(banForm, "error", "Hãy chọn một người dùng trước.");
                return;
            }

            const reason = banForm.querySelector('[name="reason"]').value.trim();
            if (!reason) {
                setFormMessage(banForm, "error", "Vui lòng nhập lý do khóa tài khoản.");
                return;
            }

            setLoading(banForm, true);

            try {
                const { response, data } = await authFetch(`/api/v1/admin/users/${currentUser.id}/ban`, {
                    method: "PATCH",
                    body: JSON.stringify({ reason })
                });

                if (!response.ok || data.success === false) {
                    setFormMessage(banForm, "error", data.message || "Không thể khóa tài khoản.");
                    return;
                }

                setFormMessage(banForm, "success", data.message || "Khóa tài khoản thành công.");
                banForm.reset();
                await loadUserDetail(currentUser.id);
            } catch (error) {
                if (error.message !== "UNAUTHORIZED") {
                    setFormMessage(banForm, "error", "Không thể khóa tài khoản lúc này.");
                }
            } finally {
                setLoading(banForm, false);
            }
        });

        unbanButton?.addEventListener("click", async () => {
            if (!currentUser) {
                setMessage(statusBox, "error", "Hãy chọn một người dùng trước.");
                return;
            }

            try {
                const { response, data } = await authFetch(`/api/v1/admin/users/${currentUser.id}/unban`, {
                    method: "PATCH"
                });

                if (!response.ok || data.success === false) {
                    setMessage(statusBox, "error", data.message || "Không thể mở khóa tài khoản.");
                    return;
                }

                setMessage(statusBox, "success", data.message || "Mở khóa tài khoản thành công.");
                await loadUserDetail(currentUser.id);
            } catch (error) {
                if (error.message !== "UNAUTHORIZED") {
                    setMessage(statusBox, "error", "Không thể mở khóa tài khoản lúc này.");
                }
            }
        });

        prevButton?.addEventListener("click", async () => {
            if (currentPage > 0) {
                currentPage -= 1;
                await loadUsers();
            }
        });

        nextButton?.addEventListener("click", async () => {
            const totalPages = Math.max(1, Math.ceil(totalElements / currentSize));
            if (currentPage + 1 < totalPages) {
                currentPage += 1;
                await loadUsers();
            }
        });

        await loadUsers();
    };

    const loadProductCoreScript = () => {
        const page = document.body?.dataset?.page;
        if (!["listing", "detail", "compare", "admin-products"].includes(page || "")) {
            return;
        }

        if (document.querySelector('script[data-product-core-script="true"]')) {
            return;
        }

        const script = document.createElement("script");
        script.src = "/js/product-core.js";
        script.defer = true;
        script.dataset.productCoreScript = "true";
        document.head.appendChild(script);
    };

    window.addEventListener(authStateEventName, () => {
        loadCurrentUserState();
    });

    window.addEventListener("storage", (event) => {
        if (event.key === "techblog.accessToken") {
            loadCurrentUserState();
        }
    });

    loadCurrentUserState();
    setupAuthForms();
    setupLogoutButtons();
    setupProfilePage();
    setupForgotPasswordPage();
    setupResetPasswordPage();
    setupAdminUsersPage();
    loadProductCoreScript();
})();
