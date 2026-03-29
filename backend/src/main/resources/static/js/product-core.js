(() => {
    const page = document.body?.dataset?.page || "";
    if (!["listing", "detail", "compare", "admin-products"].includes(page)) {
        return;
    }

    const tokenKey = "techblog.accessToken";
    const compareState = { items: [] };

    const getToken = () => localStorage.getItem(tokenKey);

    const redirectToLogin = () => {
        const next = encodeURIComponent(`${window.location.pathname}${window.location.search}`);
        window.location.href = `/login?next=${next}`;
    };

    const getToastRoot = () => {
        let root = document.querySelector("[data-product-core-toast-root]");
        if (!root) {
            root = document.createElement("div");
            root.dataset.productCoreToastRoot = "true";
            root.style.position = "fixed";
            root.style.top = "1.25rem";
            root.style.right = "1.25rem";
            root.style.zIndex = "80";
            root.style.display = "flex";
            root.style.flexDirection = "column";
            root.style.gap = "0.75rem";
            document.body.appendChild(root);
        }
        return root;
    };

    const showToast = (message, tone = "info") => {
        if (!message) {
            return;
        }

        const palette = {
            success: {
                background: "rgba(15, 33, 35, 0.95)",
                border: "1px solid rgba(34, 211, 238, 0.45)",
                color: "#dffcf8"
            },
            error: {
                background: "rgba(41, 15, 20, 0.95)",
                border: "1px solid rgba(255, 113, 108, 0.45)",
                color: "#ffe6e4"
            },
            info: {
                background: "rgba(20, 24, 34, 0.96)",
                border: "1px solid rgba(148, 163, 184, 0.3)",
                color: "#f8fafc"
            }
        };

        const style = palette[tone] || palette.info;
        const toast = document.createElement("div");
        toast.textContent = message;
        toast.style.minWidth = "16rem";
        toast.style.maxWidth = "24rem";
        toast.style.padding = "0.875rem 1rem";
        toast.style.borderRadius = "0.875rem";
        toast.style.background = style.background;
        toast.style.border = style.border;
        toast.style.color = style.color;
        toast.style.boxShadow = "0 18px 45px rgba(15, 23, 42, 0.24)";
        toast.style.opacity = "0";
        toast.style.transform = "translateY(-8px)";
        toast.style.transition = "opacity 180ms ease, transform 180ms ease";
        getToastRoot().appendChild(toast);

        requestAnimationFrame(() => {
            toast.style.opacity = "1";
            toast.style.transform = "translateY(0)";
        });

        window.setTimeout(() => {
            toast.style.opacity = "0";
            toast.style.transform = "translateY(-8px)";
            window.setTimeout(() => toast.remove(), 220);
        }, 2600);
    };

    const createHeaders = (headers, body) => {
        const result = new Headers(headers || {});
        const token = getToken();

        if (token && !result.has("Authorization")) {
            result.set("Authorization", `Bearer ${token}`);
        }

        if (body && !(body instanceof FormData) && !result.has("Content-Type")) {
            result.set("Content-Type", "application/json");
        }

        return result;
    };

    const apiFetch = async (input, init = {}, options = {}) => {
        const requestInit = { ...init };
        if (requestInit.body && !(requestInit.body instanceof FormData) && typeof requestInit.body !== "string") {
            requestInit.body = JSON.stringify(requestInit.body);
        }

        requestInit.headers = createHeaders(requestInit.headers, requestInit.body);

        const response = await fetch(input, requestInit);
        const data = await response.json().catch(() => ({}));

        if (response.status === 401 && options.redirectOnUnauthorized) {
            redirectToLogin();
            throw new Error("UNAUTHORIZED");
        }

        return { response, data };
    };

    const ensureApiSuccess = ({ response, data }, fallbackMessage) => {
        if (!response.ok || data.success === false) {
            throw new Error(data.message || fallbackMessage);
        }
        return data;
    };

    const getCompareButtons = () => document.querySelectorAll(
        ".btn-compare[data-product-id][data-product-slug], [data-compare-selected][data-product-id][data-product-slug]"
    );

    const ensureCompareBar = () => {
        let root = document.getElementById("product-core-compare-bar");
        if (!root) {
            root = document.createElement("div");
            root.id = "product-core-compare-bar";
            document.body.appendChild(root);
        }
        return root;
    };

    const buildCompareUrl = (items = []) => {
        const slugs = items
            .map((item) => item.slug)
            .filter(Boolean)
            .slice(0, 3);

        if (!slugs.length) {
            return "/compare";
        }

        const params = new URLSearchParams();
        slugs.forEach((slug) => params.append("slugs", slug));
        return `/compare?${params.toString()}`;
    };

    const syncComparePageLocation = (items = []) => {
        if (page !== "compare" || !getToken()) {
            return false;
        }

        const nextUrl = buildCompareUrl(items);
        const currentUrl = `${window.location.pathname}${window.location.search}`;
        if (nextUrl === currentUrl) {
            return false;
        }

        window.location.replace(nextUrl);
        return true;
    };

    const buildCompareButtonHtml = (button, selected) => {
        const iconSize = button.classList.contains("detail-compare-btn") ? "1rem" : "0.875rem";
        const icon = selected ? "done" : "compare_arrows";
        const label = selected ? "In Compare" : "Add to Compare";
        return `<span class="material-symbols-outlined" style="font-size:${iconSize};">${icon}</span>${label}`;
    };

    const updateCompareButtons = (items) => {
        const selectedIds = new Set(items.map((item) => String(item.productId)));
        getCompareButtons().forEach((button) => {
            if (!button.dataset.defaultHtml) {
                button.dataset.defaultHtml = button.innerHTML;
            }

            const selected = selectedIds.has(button.dataset.productId);
            button.dataset.compareSelected = selected ? "true" : "false";
            button.classList.toggle("btn-selected", selected);
            button.classList.toggle("btn-compare", !selected);
            button.setAttribute("aria-pressed", selected ? "true" : "false");
            button.innerHTML = selected ? buildCompareButtonHtml(button, true) : button.dataset.defaultHtml;
        });
    };

    const renderCompareBar = (items) => {
        const root = ensureCompareBar();
        if (page === "admin-products" || page === "compare" || !items.length) {
            root.innerHTML = "";
            return;
        }

        const thumbs = items.slice(0, 3).map((item) => {
            if (item.thumbnailUrl) {
                return `<div class="comparison-bar-thumb"><img src="${item.thumbnailUrl}" alt="${item.name}"></div>`;
            }
            return `<div class="comparison-bar-thumb-placeholder">${item.name.charAt(0).toUpperCase()}</div>`;
        }).join("");

        const placeholder = items.length < 3 ? "<div class=\"comparison-bar-thumb-placeholder\">+</div>" : "";

        root.innerHTML = `
            <div class="comparison-bar">
                <div class="comparison-bar-items">
                    <div class="comparison-bar-thumbnails">${thumbs}${placeholder}</div>
                    <div class="comparison-bar-info">
                        <span class="text-label-sm">Comparison Lab</span>
                        <span class="text-body-sm">${items.length} item(s) selected</span>
                    </div>
                </div>
                <a href="${buildCompareUrl(items)}" class="comparison-bar-btn">
                    <span class="material-symbols-outlined" style="font-size:1.25rem;">compare_arrows</span>
                    Compare Now
                </a>
            </div>
        `;
    };

    const injectCompareRemoveButtons = (items) => {
        if (page !== "compare") {
            return;
        }

        const headers = [...document.querySelectorAll(".comparison-product-header .comparison-product-header-inner")];
        headers.forEach((header, index) => {
            const item = items[index];
            let button = header.querySelector("[data-remove-compare]");

            if (!item) {
                button?.remove();
                return;
            }

            if (!button) {
                button = document.createElement("button");
                button.type = "button";
                button.dataset.removeCompare = "true";
                button.className = "btn-icon";
                button.style.color = "var(--color-on-surface-variant)";
                button.innerHTML = "<span class=\"material-symbols-outlined\" style=\"font-size:1.125rem;\">close</span>";
                header.appendChild(button);
            }

            button.title = `Remove ${item.name}`;
            button.dataset.productId = String(item.productId);
        });
    };

    const injectCompareSentimentRow = (items) => {
        if (page !== "compare") {
            return;
        }

        const tables = [...document.querySelectorAll(".comparison-table")];
        const generalTable = tables[tables.length - 1];
        if (!generalTable) {
            return;
        }

        let row = generalTable.querySelector("[data-ai-verdict-row]");
        if (!items.length) {
            row?.remove();
            return;
        }

        if (!row) {
            row = document.createElement("div");
            row.dataset.aiVerdictRow = "true";
            row.className = "comparison-table-row highlight";
            generalTable.appendChild(row);
        }

        const values = items.map((item) => {
            if (!item.sentiment) {
                return "<div class=\"comparison-table-value\"><p>N/A</p></div>";
            }

            const conclusion = item.sentiment.conclusion || "N/A";
            const positiveRatio = item.sentiment.positiveRatio ?? 0;
            return `<div class="comparison-table-value"><p>${conclusion} (${positiveRatio}% positive)</p></div>`;
        }).join("");

        row.innerHTML = `<div class="comparison-table-label">AI Verdict</div>${values}`;
    };

    const refreshCompareUi = async () => {
        if (!getToken()) {
            compareState.items = [];
            updateCompareButtons(compareState.items);
            renderCompareBar(compareState.items);
            injectCompareRemoveButtons(compareState.items);
            injectCompareSentimentRow(compareState.items);
            return compareState.items;
        }

        try {
            const { response, data } = await apiFetch("/api/v1/compare");
            compareState.items = response.ok && data.success !== false ? (data.data?.items || []) : [];
        } catch (error) {
            compareState.items = [];
        }

        updateCompareButtons(compareState.items);
        renderCompareBar(compareState.items);
        injectCompareRemoveButtons(compareState.items);
        injectCompareSentimentRow(compareState.items);

        if (syncComparePageLocation(compareState.items)) {
            return compareState.items;
        }

        return compareState.items;
    };

    const addCompareItem = async (productId) => {
        const { response, data } = await apiFetch(`/api/v1/products/${productId}/compare`, {
            method: "POST"
        }, { redirectOnUnauthorized: true });

        if (!response.ok || data.success === false) {
            throw new Error(data.message || "Could not add product to compare.");
        }

        showToast(data.message || "Product added to compare.", "success");
    };

    const removeCompareItem = async (productId) => {
        const { response, data } = await apiFetch(`/api/v1/products/${productId}/compare`, {
            method: "DELETE"
        }, { redirectOnUnauthorized: true });

        if (!response.ok || data.success === false) {
            throw new Error(data.message || "Could not remove product from compare.");
        }

        showToast(data.message || "Product removed from compare.", "success");
    };

    const bindCompareButtons = () => {
        getCompareButtons().forEach((button) => {
            if (button.dataset.compareBound === "true") {
                return;
            }

            button.dataset.compareBound = "true";
            button.addEventListener("click", async () => {
                const productId = button.dataset.productId;
                if (!productId) {
                    return;
                }

                if (!getToken()) {
                    redirectToLogin();
                    return;
                }

                const selected = button.dataset.compareSelected === "true";
                button.disabled = true;

                try {
                    if (selected) {
                        await removeCompareItem(productId);
                    } else {
                        await addCompareItem(productId);
                    }

                    await refreshCompareUi();
                } catch (error) {
                    if (error.message !== "UNAUTHORIZED") {
                        showToast(error.message || "Something went wrong.", "error");
                    }
                } finally {
                    button.disabled = false;
                }
            });
        });

        document.querySelectorAll("[data-remove-compare]").forEach((button) => {
            if (button.dataset.compareBound === "true") {
                return;
            }

            button.dataset.compareBound = "true";
            button.addEventListener("click", async () => {
                const productId = button.dataset.productId;
                if (!productId) {
                    return;
                }

                try {
                    await removeCompareItem(productId);
                    await refreshCompareUi();
                } catch (error) {
                    if (error.message !== "UNAUTHORIZED") {
                        showToast(error.message || "Something went wrong.", "error");
                    }
                }
            });
        });
    };

    const setupCompareExperience = async () => {
        if (!["listing", "detail", "compare"].includes(page)) {
            return;
        }

        bindCompareButtons();
        await refreshCompareUi();
        bindCompareButtons();
    };

    const formatPriceLabel = (value) => {
        const amount = Number(value || 0);
        if (Number.isNaN(amount)) {
            return "0 VND";
        }

        return `${new Intl.NumberFormat("vi-VN").format(amount)} VND`;
    };

    const setupListingPage = () => {
        if (page !== "listing") {
            return;
        }

        const form = document.getElementById("listing-filter-form");
        const sortInput = document.getElementById("sort-input");
        const minRatingInput = document.getElementById("min-rating-input");
        const maxPriceRange = document.getElementById("max-price-range");
        const maxPriceLabel = document.getElementById("max-price-label");
        const clearFiltersButton = document.querySelector("[data-clear-filters]");

        if (!form) {
            return;
        }

        const submitFilters = () => {
            if (typeof form.requestSubmit === "function") {
                form.requestSubmit();
                return;
            }

            form.submit();
        };

        const syncRatingButtons = () => {
            const selected = Number(minRatingInput?.value || 0);
            document.querySelectorAll(".rating-filter-btn[data-rating]").forEach((button) => {
                const rating = Number(button.dataset.rating || 0);
                const active = rating <= selected;
                button.classList.toggle("active", active);
                const icon = button.querySelector(".material-symbols-outlined");
                icon?.classList.toggle("filled", active);
            });
        };

        const syncPriceLabel = () => {
            if (!maxPriceRange || !maxPriceLabel) {
                return;
            }

            maxPriceLabel.textContent = formatPriceLabel(maxPriceRange.value || maxPriceRange.max);
        };

        syncRatingButtons();
        syncPriceLabel();

        document.querySelectorAll(".sort-tab[data-sort]").forEach((button) => {
            button.addEventListener("click", () => {
                if (sortInput) {
                    sortInput.value = button.dataset.sort || "rating";
                }
                submitFilters();
            });
        });

        form.querySelectorAll(".listing-filter-checkbox").forEach((input) => {
            input.addEventListener("change", submitFilters);
        });

        maxPriceRange?.addEventListener("input", syncPriceLabel);
        maxPriceRange?.addEventListener("change", submitFilters);

        document.querySelectorAll(".rating-filter-btn[data-rating]").forEach((button) => {
            button.addEventListener("click", () => {
                if (!minRatingInput) {
                    return;
                }

                const selectedRating = button.dataset.rating || "0";
                minRatingInput.value = minRatingInput.value === selectedRating ? "0" : selectedRating;
                syncRatingButtons();
                submitFilters();
            });
        });

        clearFiltersButton?.addEventListener("click", () => {
            form.querySelectorAll(".listing-filter-checkbox").forEach((input) => {
                input.checked = false;
            });

            if (sortInput) {
                sortInput.value = "rating";
            }

            if (minRatingInput) {
                minRatingInput.value = "0";
            }

            if (maxPriceRange) {
                maxPriceRange.value = maxPriceRange.dataset.max || maxPriceRange.max || "0";
            }

            syncRatingButtons();
            syncPriceLabel();
            submitFilters();
        });

        document.querySelectorAll(".product-card-image img").forEach((image) => {
            if (image.dataset.fallbackBound === "true") {
                return;
            }

            image.dataset.fallbackBound = "true";
            image.addEventListener("error", () => {
                image.style.display = "none";
                if (image.parentElement?.querySelector(".product-card-image-placeholder")) {
                    return;
                }

                const placeholder = document.createElement("div");
                placeholder.className = "product-card-image-placeholder";
                placeholder.innerHTML = "<span class=\"material-symbols-outlined\">image</span>";
                image.parentElement?.prepend(placeholder);
            });
        });
    };

    const setupDetailPage = () => {
        if (page !== "detail") {
            return;
        }

        const heroImage = document.getElementById("gallery-hero-image");
        const galleryHero = document.querySelector(".gallery-hero");
        const fullscreenButton = document.getElementById("gallery-fullscreen-btn");
        const tocLinks = [...document.querySelectorAll(".detail-toc-nav a[href^='#']")];

        document.querySelectorAll(".gallery-thumb[data-gallery-image]").forEach((button) => {
            button.addEventListener("click", () => {
                if (!heroImage) {
                    return;
                }

                heroImage.src = button.dataset.galleryImage || "";
                heroImage.alt = button.dataset.galleryAlt || heroImage.alt || "Product image";
                document.querySelectorAll(".gallery-thumb").forEach((thumb) => thumb.classList.remove("active"));
                button.classList.add("active");
            });
        });

        heroImage?.addEventListener("error", () => {
            heroImage.style.opacity = "0";
            galleryHero?.classList.add("gallery-hero-empty");
        });

        fullscreenButton?.addEventListener("click", async () => {
            if (!galleryHero || !document.fullscreenEnabled) {
                return;
            }

            try {
                if (document.fullscreenElement) {
                    await document.exitFullscreen();
                    return;
                }

                await galleryHero.requestFullscreen();
            } catch (error) {
                showToast("Could not toggle fullscreen gallery.", "error");
            }
        });

        if (!tocLinks.length) {
            return;
        }

        const sections = tocLinks
            .map((link) => document.querySelector(link.getAttribute("href")))
            .filter(Boolean);

        const syncActiveSection = () => {
            const offset = window.scrollY + 180;
            let currentSectionId = sections[0]?.id || "";

            sections.forEach((section) => {
                if (section.offsetTop <= offset) {
                    currentSectionId = section.id;
                }
            });

            tocLinks.forEach((link) => {
                link.classList.toggle("active", link.getAttribute("href") === `#${currentSectionId}`);
            });
        };

        window.addEventListener("scroll", syncActiveSection, { passive: true });
        syncActiveSection();
    };

    const setupAdminProductsPage = async () => {
        if (page !== "admin-products") {
            return;
        }

        const refs = {
            authForm: document.getElementById("admin-auth-form"),
            authEmail: document.getElementById("admin-auth-email"),
            authPassword: document.getElementById("admin-auth-password"),
            clearToken: document.getElementById("admin-clear-token"),
            authStatus: document.getElementById("admin-auth-status"),
            openCreate: document.getElementById("open-create-product"),
            overlay: document.getElementById("product-modal-overlay"),
            closeButtons: [...document.querySelectorAll("[data-close-product-modal]")],
            modalTitle: document.getElementById("product-modal-title"),
            modalSubtitle: document.getElementById("product-modal-subtitle"),
            modalStatus: document.getElementById("product-modal-status"),
            saveButton: document.getElementById("save-product-button"),
            tabs: [...document.querySelectorAll(".modal-tab")],
            panels: [...document.querySelectorAll("[data-tab-panel]")],
            addImageRow: document.getElementById("add-image-row"),
            imageList: document.getElementById("modal-images-grid"),
            imageTemplate: document.getElementById("admin-image-card-template"),
            addSpecRow: document.getElementById("add-spec-row"),
            specList: document.getElementById("spec-rows"),
            specTemplate: document.getElementById("admin-spec-row-template"),
            name: document.getElementById("product-name-input"),
            slug: document.getElementById("product-slug-input"),
            brand: document.getElementById("product-brand-input"),
            model: document.getElementById("product-model-input"),
            category: document.getElementById("product-category-input"),
            status: document.getElementById("product-status-input"),
            price: document.getElementById("product-price-input"),
            currency: document.getElementById("product-currency-input"),
            shortDescription: document.getElementById("product-short-description-input"),
            description: document.getElementById("product-description-input"),
            allowComments: document.getElementById("product-allow-comments-input"),
            selectAll: document.getElementById("select-all"),
            selectedCount: document.getElementById("selected-count")
        };

        const state = { currentProductId: null };

        const setAuthStatus = (message, tone = "warning") => {
            if (!refs.authStatus) {
                return;
            }

            refs.authStatus.textContent = message;
            refs.authStatus.dataset.state = tone;
        };

        const setModalStatus = (message) => {
            if (refs.modalStatus) {
                refs.modalStatus.textContent = message || "Ready";
            }
        };

        const setSaveLoading = (loading) => {
            if (!refs.saveButton) {
                return;
            }

            refs.saveButton.disabled = loading;
            refs.saveButton.textContent = loading ? "Saving..." : "Save Product";
        };

        const switchTab = (target) => {
            refs.tabs.forEach((tab) => {
                tab.classList.toggle("active", tab.dataset.tabTarget === target);
            });

            refs.panels.forEach((panel) => {
                panel.hidden = panel.dataset.tabPanel !== target;
            });
        };

        const updateSelectionCount = () => {
            if (!refs.selectedCount) {
                return;
            }

            const count = document.querySelectorAll(".product-checkbox:checked").length;
            refs.selectedCount.textContent = `${count} item(s) selected`;
        };

        const clearForm = () => {
            refs.name.value = "";
            refs.slug.value = "";
            refs.brand.value = "";
            refs.model.value = "";
            refs.category.value = "";
            refs.status.value = "DRAFT";
            refs.price.value = "";
            refs.currency.value = "VND";
            refs.shortDescription.value = "";
            refs.description.value = "";
            refs.allowComments.checked = true;
            refs.imageList.innerHTML = "";
            refs.specList.innerHTML = "";
            state.currentProductId = null;
            setModalStatus("Ready");
        };

        const updateImagePreview = (row) => {
            const preview = row.querySelector(".admin-image-preview");
            const placeholder = row.querySelector(".admin-image-preview-empty");
            const urlInput = row.querySelector(".admin-image-url");
            const nextUrl = urlInput.value.trim();

            if (nextUrl) {
                preview.src = nextUrl;
                preview.style.display = "block";
                placeholder.style.display = "none";
            } else {
                preview.removeAttribute("src");
                preview.style.display = "none";
                placeholder.style.display = "flex";
            }
        };

        const enforceSinglePrimary = (currentCheckbox) => {
            if (!currentCheckbox.checked) {
                return;
            }

            refs.imageList.querySelectorAll(".admin-image-primary").forEach((checkbox) => {
                if (checkbox !== currentCheckbox) {
                    checkbox.checked = false;
                }
            });
        };

        const addImageRow = (image = {}) => {
            const fragment = refs.imageTemplate.content.cloneNode(true);
            const row = fragment.querySelector(".image-card-row");
            if (image.id) {
                row.dataset.imageId = String(image.id);
            }

            const urlInput = row.querySelector(".admin-image-url");
            const altInput = row.querySelector(".admin-image-alt");
            const orderInput = row.querySelector(".admin-image-order");
            const primaryInput = row.querySelector(".admin-image-primary");
            const removeButton = row.querySelector(".admin-remove-image-row");

            urlInput.value = image.imageUrl || "";
            altInput.value = image.altText || "";
            orderInput.value = image.displayOrder ?? 0;
            primaryInput.checked = Boolean(image.primary);

            urlInput.addEventListener("input", () => updateImagePreview(row));
            primaryInput.addEventListener("change", () => enforceSinglePrimary(primaryInput));
            removeButton.addEventListener("click", () => row.remove());

            refs.imageList.appendChild(row);
            updateImagePreview(row);
        };

        const addSpecRow = (spec = {}) => {
            const fragment = refs.specTemplate.content.cloneNode(true);
            const row = fragment.querySelector(".admin-spec-row-item");
            if (spec.id) {
                row.dataset.specId = String(spec.id);
            }

            row.querySelector(".admin-spec-key-input").value = spec.specKey || "";
            row.querySelector(".admin-spec-value-input").value = spec.specValue || "";
            row.querySelector(".admin-spec-unit-input").value = spec.unit || "";
            row.querySelector(".admin-spec-order-input").value = spec.displayOrder ?? 0;
            row.querySelector(".admin-remove-spec-row").addEventListener("click", () => row.remove());
            refs.specList.appendChild(row);
        };

        const openModal = () => {
            refs.overlay.classList.add("open");
            document.body.style.overflow = "hidden";
            switchTab("basic");
        };

        const closeModal = () => {
            refs.overlay.classList.remove("open");
            document.body.style.overflow = "";
            clearForm();
        };

        const populateForm = (product) => {
            state.currentProductId = product.id;
            refs.name.value = product.name || "";
            refs.slug.value = product.slug || "";
            refs.brand.value = product.brand || "";
            refs.model.value = product.model || "";
            refs.category.value = product.categoryId || "";
            refs.status.value = product.status || "DRAFT";
            refs.price.value = product.price ?? "";
            refs.currency.value = product.currency || "VND";
            refs.shortDescription.value = product.shortDescription || "";
            refs.description.value = product.description || "";
            refs.allowComments.checked = product.allowComments !== false;
            refs.imageList.innerHTML = "";
            refs.specList.innerHTML = "";

            (product.images || []).forEach(addImageRow);
            (product.specs || []).forEach(addSpecRow);

            refs.modalTitle.textContent = "Edit Product";
            refs.modalSubtitle.textContent = product.status || "Draft mode";
            setModalStatus(`Editing ${product.name}`);
        };

        const readProductPayload = () => ({
            name: refs.name.value.trim(),
            slug: refs.slug.value.trim(),
            brand: refs.brand.value.trim(),
            model: refs.model.value.trim(),
            categoryId: refs.category.value ? Number(refs.category.value) : null,
            price: refs.price.value ? Number(refs.price.value) : 0,
            currency: (refs.currency.value || "VND").trim().toUpperCase(),
            shortDescription: refs.shortDescription.value.trim(),
            description: refs.description.value.trim(),
            allowComments: refs.allowComments.checked
        });

        const collectImages = () => [...refs.imageList.querySelectorAll(".image-card-row")]
                .map((row) => ({
                    id: row.dataset.imageId ? Number(row.dataset.imageId) : null,
                    imageUrl: row.querySelector(".admin-image-url").value.trim(),
                    altText: row.querySelector(".admin-image-alt").value.trim(),
                    displayOrder: Number(row.querySelector(".admin-image-order").value || 0),
                    primary: row.querySelector(".admin-image-primary").checked
                }))
                .filter((item) => item.imageUrl);

        const collectSpecs = () => [...refs.specList.querySelectorAll(".admin-spec-row-item")]
                .map((row) => ({
                    id: row.dataset.specId ? Number(row.dataset.specId) : null,
                    specKey: row.querySelector(".admin-spec-key-input").value.trim(),
                    specValue: row.querySelector(".admin-spec-value-input").value.trim(),
                    unit: row.querySelector(".admin-spec-unit-input").value.trim(),
                    displayOrder: Number(row.querySelector(".admin-spec-order-input").value || 0)
                }))
                .filter((item) => item.specKey || item.specValue);

        const validateProduct = () => {
            const payload = readProductPayload();
            if (!payload.name || !payload.slug || !payload.brand || !payload.shortDescription || !payload.description) {
                return "Please complete all required product fields before saving.";
            }

            if (Number.isNaN(payload.price) || payload.price < 0) {
                return "Price must be a non-negative number.";
            }

            const invalidSpec = collectSpecs().find((spec) => !spec.specKey || !spec.specValue);
            if (invalidSpec) {
                return "Each spec row needs both a key and a value.";
            }

            return null;
        };

        const fetchProfile = async () => {
            if (!getToken()) {
                return null;
            }

            const { response, data } = await apiFetch("/api/v1/users/me");
            if (!response.ok || data.success === false) {
                return null;
            }

            return data.data || null;
        };

        const ensureAdminProfile = async () => {
            const profile = await fetchProfile();
            if (!profile) {
                setAuthStatus("No admin token found. Sign in to use product actions.", "warning");
                return null;
            }

            if (!(profile.roles || []).includes("ADMIN")) {
                setAuthStatus("Current token is not an admin account.", "error");
                return null;
            }

            setAuthStatus(`Connected as ${profile.email}`, "success");
            return profile;
        };

        const fetchAdminProduct = async (productId) => {
            const { response, data } = await apiFetch(`/api/v1/admin/products/${productId}`);
            if (!response.ok || data.success === false) {
                throw new Error(data.message || "Could not load product details.");
            }
            return data.data;
        };

        const syncSpecs = async (productId, desiredSpecs) => {
            const existing = (await fetchAdminProduct(productId)).specs || [];
            const desiredIds = new Set(desiredSpecs.filter((spec) => spec.id).map((spec) => String(spec.id)));

            for (const spec of existing) {
                if (!desiredIds.has(String(spec.id))) {
                    ensureApiSuccess(
                        await apiFetch(`/api/v1/admin/products/${productId}/specs/${spec.id}`, { method: "DELETE" }),
                        "Could not remove a product spec."
                    );
                }
            }

            for (const spec of desiredSpecs) {
                const payload = {
                    specKey: spec.specKey,
                    specValue: spec.specValue,
                    unit: spec.unit || null,
                    displayOrder: spec.displayOrder
                };

                if (spec.id) {
                    ensureApiSuccess(
                        await apiFetch(`/api/v1/admin/products/${productId}/specs/${spec.id}`, {
                            method: "PUT",
                            body: payload
                        }),
                        "Could not update a product spec."
                    );
                } else {
                    ensureApiSuccess(
                        await apiFetch(`/api/v1/admin/products/${productId}/specs`, {
                            method: "POST",
                            body: payload
                        }),
                        "Could not create a product spec."
                    );
                }
            }
        };

        const matchImage = (serverImage, desiredImage) => {
            if (desiredImage.id) {
                return Number(serverImage.id) === Number(desiredImage.id);
            }

            return serverImage.imageUrl === desiredImage.imageUrl
                && Number(serverImage.displayOrder || 0) === Number(desiredImage.displayOrder || 0)
                && (serverImage.altText || "") === (desiredImage.altText || "");
        };

        const syncImages = async (productId, desiredImages) => {
            const existing = (await fetchAdminProduct(productId)).images || [];
            const desiredIds = new Set(desiredImages.filter((image) => image.id).map((image) => String(image.id)));

            for (const image of existing) {
                if (!desiredIds.has(String(image.id))) {
                    ensureApiSuccess(
                        await apiFetch(`/api/v1/admin/products/${productId}/images/${image.id}`, { method: "DELETE" }),
                        "Could not remove a product image."
                    );
                }
            }

            for (const image of desiredImages) {
                const payload = {
                    imageUrl: image.imageUrl,
                    altText: image.altText || null,
                    primary: false,
                    displayOrder: image.displayOrder
                };

                if (image.id) {
                    ensureApiSuccess(
                        await apiFetch(`/api/v1/admin/products/${productId}/images/${image.id}`, {
                            method: "PUT",
                            body: payload
                        }),
                        "Could not update a product image."
                    );
                } else {
                    ensureApiSuccess(
                        await apiFetch(`/api/v1/admin/products/${productId}/images`, {
                            method: "POST",
                            body: payload
                        }),
                        "Could not create a product image."
                    );
                }
            }

            if (!desiredImages.length) {
                return;
            }

            const primaryCandidate = desiredImages.find((image) => image.primary) || desiredImages[0];
            const refreshed = await fetchAdminProduct(productId);
            const target = (refreshed.images || []).find((image) => matchImage(image, primaryCandidate));
            if (target && !target.primary) {
                ensureApiSuccess(
                    await apiFetch(`/api/v1/admin/products/${productId}/images/${target.id}/main`, {
                        method: "PATCH"
                    }),
                    "Could not update the main product image."
                );
            }
        };

        const saveProduct = async () => {
            const profile = await ensureAdminProfile();
            if (!profile) {
                return;
            }

            const validationMessage = validateProduct();
            if (validationMessage) {
                setModalStatus(validationMessage);
                showToast(validationMessage, "error");
                return;
            }

            setSaveLoading(true);
            setModalStatus("Saving product...");

            try {
                const payload = readProductPayload();
                const endpoint = state.currentProductId ? `/api/v1/admin/products/${state.currentProductId}` : "/api/v1/admin/products";
                const method = state.currentProductId ? "PUT" : "POST";
                const { response, data } = await apiFetch(endpoint, { method, body: payload });

                if (!response.ok || data.success === false) {
                    throw new Error(data.message || "Could not save product.");
                }

                const savedProduct = data.data;
                const targetStatus = refs.status.value;
                if (targetStatus && savedProduct.status !== targetStatus) {
                    const statusResult = await apiFetch(`/api/v1/admin/products/${savedProduct.id}/status`, {
                        method: "PATCH",
                        body: { status: targetStatus }
                    });
                    if (!statusResult.response.ok || statusResult.data.success === false) {
                        throw new Error(statusResult.data.message || "Could not update product status.");
                    }
                }

                await syncSpecs(savedProduct.id, collectSpecs());
                await syncImages(savedProduct.id, collectImages());

                showToast("Product saved successfully.", "success");
                closeModal();
                window.location.reload();
            } catch (error) {
                if (error.message !== "UNAUTHORIZED") {
                    setModalStatus(error.message || "Could not save product.");
                    showToast(error.message || "Could not save product.", "error");
                }
            } finally {
                setSaveLoading(false);
            }
        };

        const toggleProductStatus = async (productId, currentStatus) => {
            const profile = await ensureAdminProfile();
            if (!profile) {
                return;
            }

            const nextStatus = currentStatus === "PUBLISHED" ? "HIDDEN" : "PUBLISHED";
            try {
                const { response, data } = await apiFetch(`/api/v1/admin/products/${productId}/status`, {
                    method: "PATCH",
                    body: { status: nextStatus }
                });
                if (!response.ok || data.success === false) {
                    throw new Error(data.message || "Could not update product status.");
                }
                showToast(`Product moved to ${nextStatus}.`, "success");
                window.location.reload();
            } catch (error) {
                if (error.message !== "UNAUTHORIZED") {
                    showToast(error.message || "Could not update product status.", "error");
                }
            }
        };

        const deleteProduct = async (productId, productName) => {
            const profile = await ensureAdminProfile();
            if (!profile || !window.confirm(`Delete ${productName || "this product"}?`)) {
                return;
            }

            try {
                const { response, data } = await apiFetch(`/api/v1/admin/products/${productId}`, { method: "DELETE" });
                if (!response.ok || data.success === false) {
                    throw new Error(data.message || "Could not delete product.");
                }
                showToast("Product deleted successfully.", "success");
                window.location.reload();
            } catch (error) {
                if (error.message !== "UNAUTHORIZED") {
                    showToast(error.message || "Could not delete product.", "error");
                }
            }
        };

        const restoreProduct = async (productId) => {
            const profile = await ensureAdminProfile();
            if (!profile) {
                return;
            }

            try {
                const { response, data } = await apiFetch(`/api/v1/admin/products/${productId}/restore`, { method: "PATCH" });
                if (!response.ok || data.success === false) {
                    throw new Error(data.message || "Could not restore product.");
                }
                showToast("Product restored successfully.", "success");
                window.location.reload();
            } catch (error) {
                if (error.message !== "UNAUTHORIZED") {
                    showToast(error.message || "Could not restore product.", "error");
                }
            }
        };

        const bindTableActions = () => {
            document.querySelectorAll(".btn-edit-product").forEach((button) => {
                if (button.dataset.bound === "true") {
                    return;
                }
                button.dataset.bound = "true";
                button.addEventListener("click", async () => {
                    try {
                        const profile = await ensureAdminProfile();
                        if (!profile) {
                            return;
                        }
                        const product = await fetchAdminProduct(button.dataset.productId);
                        populateForm(product);
                        openModal();
                    } catch (error) {
                        if (error.message !== "UNAUTHORIZED") {
                            showToast(error.message || "Could not load product.", "error");
                        }
                    }
                });
            });

            document.querySelectorAll(".btn-status-product").forEach((button) => {
                if (button.dataset.bound === "true") {
                    return;
                }
                button.dataset.bound = "true";
                button.addEventListener("click", () => toggleProductStatus(button.dataset.productId, button.dataset.currentStatus));
            });

            document.querySelectorAll(".btn-delete-product").forEach((button) => {
                if (button.dataset.bound === "true") {
                    return;
                }
                button.dataset.bound = "true";
                button.addEventListener("click", () => deleteProduct(button.dataset.productId, button.dataset.productName));
            });

            document.querySelectorAll(".btn-restore-product").forEach((button) => {
                if (button.dataset.bound === "true") {
                    return;
                }
                button.dataset.bound = "true";
                button.addEventListener("click", () => restoreProduct(button.dataset.productId));
            });

            document.querySelectorAll(".product-checkbox").forEach((checkbox) => {
                checkbox.addEventListener("change", updateSelectionCount);
            });
        };

        refs.tabs.forEach((tab) => {
            tab.addEventListener("click", () => switchTab(tab.dataset.tabTarget));
        });

        refs.openCreate?.addEventListener("click", async () => {
            const profile = await ensureAdminProfile();
            if (!profile) {
                return;
            }

            clearForm();
            refs.modalTitle.textContent = "Create Product";
            refs.modalSubtitle.textContent = "Draft mode";
            addImageRow();
            addSpecRow();
            openModal();
        });

        refs.closeButtons.forEach((button) => button.addEventListener("click", closeModal));
        refs.overlay?.addEventListener("click", (event) => {
            if (event.target === refs.overlay) {
                closeModal();
            }
        });

        document.addEventListener("keydown", (event) => {
            if (event.key === "Escape" && refs.overlay.classList.contains("open")) {
                closeModal();
            }
        });

        refs.addImageRow?.addEventListener("click", () => addImageRow());
        refs.addSpecRow?.addEventListener("click", () => addSpecRow());
        refs.saveButton?.addEventListener("click", saveProduct);

        refs.authForm?.addEventListener("submit", async (event) => {
            event.preventDefault();
            const email = refs.authEmail.value.trim();
            const password = refs.authPassword.value;
            if (!email || !password) {
                setAuthStatus("Enter email and password to connect.", "error");
                return;
            }

            setAuthStatus("Connecting admin session...", "warning");

            try {
                const response = await fetch("/api/v1/auth/login", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email, password })
                });
                const data = await response.json().catch(() => ({}));
                if (!response.ok || data.success === false || !data.data?.accessToken) {
                    throw new Error(data.message || "Could not sign in.");
                }

                localStorage.setItem(tokenKey, data.data.accessToken);
                refs.authPassword.value = "";
                await ensureAdminProfile();
                showToast("Admin token connected.", "success");
            } catch (error) {
                setAuthStatus(error.message || "Could not sign in.", "error");
            }
        });

        refs.clearToken?.addEventListener("click", () => {
            localStorage.removeItem(tokenKey);
            setAuthStatus("Admin token cleared.", "warning");
        });

        refs.selectAll?.addEventListener("change", () => {
            document.querySelectorAll(".product-checkbox").forEach((checkbox) => {
                checkbox.checked = refs.selectAll.checked;
            });
            updateSelectionCount();
        });

        bindTableActions();
        updateSelectionCount();
        await ensureAdminProfile();
    };

    setupListingPage();
    setupDetailPage();
    setupCompareExperience();
    setupAdminProductsPage();
})();
