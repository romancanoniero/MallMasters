# Mall Masters deployment on shared Contabo VPS

**Do not deploy blindly:** several unrelated Docker applications already use this server. Inspect current domains, reverse proxy, compose projects, mounts, resource usage, and backups before touching configuration.

Target staging URL: https://improvingyourreality.com/mallmasters/

The single-file prototype is `apps/visitor-web/index.html`. It is static and requires no backend, credentials, or new network ports.

## Safe procedure once SSH access and web root are verified
1. Read existing reverse-proxy rules and identify the web root or owning proxy container. Never assume it is /var/www/html.
2. Back up the current site/proxy configuration outside the affected directory.
3. Create an isolated `mallmasters` directory in the actual document root with minimal privileges.
4. Copy the visitor-web `index.html` there without replacing the parent index.
5. If proxy routing is necessary, add only an exact /mallmasters/ route to the existing proxy config; test before reload.
6. Run an HTTPS smoke test and ensure unrelated app paths still work.
7. Provide a rollback that removes only the Mall Masters route and directory.
8. Rotate previously shared login/VNC passwords and move toward SSH keys.

No VPS writes are performed by this repository. The demo is conceptual, not live navigation data.
