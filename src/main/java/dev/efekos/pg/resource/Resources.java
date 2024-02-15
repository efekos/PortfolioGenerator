/*
 * Copyright 2024 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package dev.efekos.pg.resource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Resources {
    public static final Resource HTML_ABOUT_ENTRY_TEMPLATE = new Resource("/site/html/template/about_entry.html", true);
    public static final Resource HTML_CURRENT_JOB_TEMPLATE = new Resource("/site/html/template/current_job.html", true);
    public static final Resource HTML_EDUCATION_ENTRY_TEMPLATE = new Resource("/site/html/template/education_entry.html", true);
    public static final Resource HTML_EXPERIENCE_ENTRY_TEMPLATE = new Resource("/site/html/template/experience_entry.html", true);
    public static final Resource HTML_FOOTER = new Resource("/site/html/template/footer.html", true);
    public static final Resource HTML_PAGINATION_BUTTONS = new Resource("/site/html/template/pagination_buttons.html", true);
    public static final Resource HTML_PLACE_ENTRY_TEMPLATE = new Resource("/site/html/template/place_entry.html", true);
    public static final Resource HTML_PROJECT_GALLERY_IMAGE_TEMPLATE = new Resource("/site/html/template/project_gallery_image_template.html", true);
    public static final Resource HTML_PROJECT_RELEASES_ELEMENT = new Resource("/site/html/template/project_releases.html", true);
    public static final Resource HTML_PROJECT_VERSION_ENTRY_TEMPLATE = new Resource("/site/html/template/project_version_entry.html", true);
    public static final Resource HTML_PROJECT_ENTRY_TEMPLATE = new Resource("/site/html/template/projects_element_template.html", true);
    public static final Resource HTML_SOCIAL_ICON_TEMPLATE = new Resource("/site/html/template/social_icon.html", true);
    public static final Resource HTML_BIO_PAGE = new Resource("/site/html/bio.html");
    public static final Resource HTML_CERTIFICATES_PAGE = new Resource("/site/html/certificates.html");
    public static final Resource HTML_CONTACT_PAGE = new Resource("/site/html/contact.html");
    public static final Resource HTML_EDUCATION_PAGE = new Resource("/site/html/education.html");
    public static final Resource HTML_EXPERIENCE_PAGE = new Resource("/site/html/experience.html");
    public static final Resource HTML_INDEX_PAGE = new Resource("/site/html/index.html");
    public static final Resource HTML_PROJECT_PAGE = new Resource("/site/html/project.html");
    public static final Resource HTML_PROJECT_CHANGELOG_PAGE = new Resource("/site/html/project_changelog.html");
    public static final Resource HTML_PROJECT_GALLERY_PAGE = new Resource("/site/html/project_gallery.html");
    public static final Resource HTML_PROJECT_LICENSE_PAGE = new Resource("/site/html/project_license.html");
    public static final Resource HTML_PROJECT_VERSIONS_PAGE = new Resource("/site/html/project_versions.html");
    public static final Resource HTML_PROJECTS_PAGE = new Resource("/site/html/projects.html");
    public static final Resource HTML_SINGLE_CERTIFICATE = new Resource("/site/html/single_certificate.html");
    public static final Resource ICON_SOURCE = new Resource("/site/icon/source.svg");
    public static final Resource ICON_PROJECT_DOCUMENTATION = new Resource("/site/icon/project_link/doc.svg");
    public static final Resource ICON_PROJECT_GUIDE = new Resource("/site/icon/project_link/guide.svg");
    public static final Resource ICON_PROJECT_ISSUES = new Resource("/site/icon/project_link/issues.svg");
    public static final Resource ICON_PROJECT_MAIN_WEBSITE = new Resource("/site/icon/project_link/main.svg");
    public static final Resource ICON_PROJECT_SOURCE = new Resource("/site/icon/project_link/source.svg");
    public static final Resource ICON_PROJECT_SUPPORT = new Resource("/site/icon/project_link/support.svg");
    public static final Resource ICON_PROJECT_WIKI = new Resource("/site/icon/project_link/wiki.svg");
    public static final Resource ICON_BIONLUK = new Resource("/site/icon/social/bionluk.svg");
    public static final Resource ICON_BUY_ME_A_COFFEE = new Resource("/site/icon/social/buy_me_a_coffee.svg");
    public static final Resource ICON_CROWDIN = new Resource("/site/icon/social/crowdin.svg");
    public static final Resource ICON_CURSEFORGE = new Resource("/site/icon/social/curseforge.svg");
    public static final Resource ICON_DISCORD = new Resource("/site/icon/social/discord.svg");
    public static final Resource ICON_FACEBOOK = new Resource("/site/icon/social/facebook.svg");
    public static final Resource ICON_FIVERR = new Resource("/site/icon/social/fiverr.svg");
    public static final Resource ICON_GITHUB = new Resource("/site/icon/social/github.svg");
    public static final Resource ICON_INSTAGRAM = new Resource("/site/icon/social/instagram.svg");
    public static final Resource ICON_KICK = new Resource("/site/icon/social/kick.svg");
    public static final Resource ICON_KOFI = new Resource("/site/icon/social/kofi.svg");
    public static final Resource ICON_LINKEDIN = new Resource("/site/icon/social/linkedin.svg");
    public static final Resource ICON_MODRINTH = new Resource("/site/icon/social/modrinth.svg");
    public static final Resource ICON_NPM = new Resource("/site/icon/social/npm.svg");
    public static final Resource ICON_PATREON = new Resource("/site/icon/social/patreon.svg");
    public static final Resource ICON_REDDIT = new Resource("/site/icon/social/reddit.svg");
    public static final Resource ICON_SNAPCHAT = new Resource("/site/icon/social/snapchat.svg");
    public static final Resource ICON_SPOTIFY = new Resource("/site/icon/social/spotify.svg");
    public static final Resource ICON_STACKOVERFLOW = new Resource("/site/icon/social/stackoverflow.svg");
    public static final Resource ICON_STEAM = new Resource("/site/icon/social/steam.svg");
    public static final Resource ICON_THREADS = new Resource("/site/icon/social/threads.svg");
    public static final Resource ICON_TIKTOK = new Resource("/site/icon/social/tiktok.svg");
    public static final Resource ICON_TWITCH = new Resource("/site/icon/social/twitch.svg");
    public static final Resource ICON_TWITTER = new Resource("/site/icon/social/twitter.svg");
    public static final Resource ICON_VSCODE_MARKETPLACE = new Resource("/site/icon/social/vscode_marketplace.svg");
    public static final Resource ICON_X = new Resource("/site/icon/social/x.svg");
    public static final Resource ICON_YOUTUBE = new Resource("/site/icon/social/youtube.svg");
    public static final Resource ICON_CAKE = new Resource("/site/icon/birth.svg");
    public static final Resource ICON_BOOK_OPEN = new Resource("/site/icon/book_open.svg");
    public static final Resource ICON_BRIEFCASE = new Resource("/site/icon/briefcase.svg");
    public static final Resource ICON_CLOCK = new Resource("/site/icon/clock.svg");
    public static final Resource ICON_DEGREE = new Resource("/site/icon/degree.svg");
    public static final Resource ICON_EXPANDED = new Resource("/site/icon/expanded.svg");
    public static final Resource ICON_EXTERNAL_WEBSITE = new Resource("/site/icon/external_site.svg");
    public static final Resource ICON_LANGUAGE = new Resource("/site/icon/language.svg");
    public static final Resource ICON_LETTER = new Resource("/site/icon/letter.svg");
    public static final Resource ICON_LOCATION = new Resource("/site/icon/location.svg");
    public static final Resource ICON_PHONE = new Resource("/site/icon/phone.svg");
    public static final Resource ICON_PROJECT = new Resource("/site/icon/project.svg");
    public static final Resource ICON_SCALE = new Resource("/site/icon/scale.svg");
    public static final Resource ICON_SCHOOL = new Resource("/site/icon/school.svg");
    public static final Resource ICON_TAG = new Resource("/site/icon/tag.svg");
    public static final Resource ICON_TOP = new Resource("/site/icon/top.svg");
    public static final Resource ICON_UNEXPANDED = new Resource("/site/icon/unexpanded.svg");
    public static final Resource ICON_UNIVERSITY = new Resource("/site/icon/university.svg");
    public static final Resource SCRIPT_LIBRARY_MARKED = new Resource("/site/lib/marked.js");
    public static final Resource SCRIPT_LIBRARY_PRISM = new Resource("/site/lib/prism.js");
    public static final Resource STYLE_LIBRARY_PRISM = new Resource("/site/lib/prism.css");
    public static final Resource SCRIPT_AGE_CALCULATOR = new Resource("/site/script/age_calculator.js");
    public static final Resource SCRIPT_EXPAND_ENTRIES = new Resource("/site/script/expandable_entries.js");
    public static final Resource SCRIPT_GALLERY_MODAL_TEMPLATE = new Resource("/site/script/gallery_modal_template.js", true);
    public static final Resource SCRIPT_CHANGELOG_FINDER = new Resource("/site/script/project_changelog_finder.js");
    public static final Resource SCRIPT_README_FINDER = new Resource("/site/script/project_readme_finder.js");
    public static final Resource SCRIPT_VERSIONS_GITHUB_RELEASE_FINDER = new Resource("/site/script/project_versions_grl_finder.js");
    public static final Resource SCRIPT_VERSIONS_JSON_FINDER = new Resource("/site/script/project_versions_json_finder.js");
    public static final Resource SCRIPT_VERSIONS_MARKDOWN_FINDER = new Resource("/site/script/project_versions_markdown_file_finder.js");
    public static final Resource SCRIPT_MODRINTH_RELEASE_FINDER = new Resource("/site/script/project_versions_mrl_finder.js");
    public static final Resource SCRIPT_SPIGOT_UPDATE_FINDER = new Resource("/site/script/project_versions_spig_finder.js");
    public static final Resource SCRIPT_PROJECT_SEARCH = new Resource("/site/script/projects_search.js");
    public static final Resource STYLE_SOCIAL_ICON_TEMPLATE = new Resource("/site/style/template/social_icon.css", true);
    public static final Resource STYLE_PROJECT_TAG_TEMPLATE = new Resource("/site/style/template/style_project_tag.css", true);
    public static final Resource STYLE_PROJECT_VERSION_TAG_TEMPLATE = new Resource("/site/style/template/version_tag.css", true);
    public static final Resource STYLE_MAIN = new Resource("/site/style/style.css");
    public static final Resource STYLE_CERTIFICATES = new Resource("/site/style/style_certificates.css");
    public static final Resource STYLE_EDUCATION = new Resource("/site/style/style_education.css");
    public static final Resource STYLE_GALLERY_MODALS = new Resource("/site/style/style_gallery_modals.css");
    public static final Resource STYLE_PROJECTS = new Resource("/site/style/style_projects.css");
    public static final Resource JSON_VALID_LOCALES = new Resource("/valid_locales.json");
    public static final Resource JSON_CSS_COLOR_NAMES = new Resource("/css_color_names.json");
    public static final Resource JSON_DEFAULT_COLOR_THEME = new Resource("/site/default_color_theme.json");

    private static final List<Resource> list = new ArrayList<>();

    public static List<Resource> all() {
        if (!list.isEmpty()) return list;
        Field[] fields = Resources.class.getFields();

        for (Field field : fields) {
            try {
                Resource resource = (Resource) field.get(null);
                list.add(resource);
            } catch (IllegalAccessException ignored) {
            }
        }

        return list;
    }
}