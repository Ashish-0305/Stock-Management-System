
define(['knockout', 'ojs/ojcontext', 'ojs/ojmodule-element-utils', 'ojs/ojresponsiveutils', 'ojs/ojresponsiveknockoututils', 'ojs/ojcorerouter', 'ojs/ojmodulerouter-adapter', 'ojs/ojknockoutrouteradapter', 'ojs/ojurlparamadapter', 'ojs/ojarraydataprovider', 'ojs/ojknockouttemplateutils', 'ojs/ojmodule-element', 'ojs/ojknockout'],
  function(ko, Context, moduleUtils, ResponsiveUtils, ResponsiveKnockoutUtils, CoreRouter, ModuleRouterAdapter, KnockoutRouterAdapter, UrlParamAdapter, ArrayDataProvider, KnockoutTemplateUtils) {

     function ControllerViewModel() {
        
        this.KnockoutTemplateUtils = KnockoutTemplateUtils;

        // Handle announcements sent when pages change, for Accessibility.
        this.manner = ko.observable('polite');
        this.message = ko.observable();

        announcementHandler = (event) => {
          this.message(event.detail.message);
          this.manner(event.detail.manner);
      };

      document.getElementById('globalBody').addEventListener('announce', announcementHandler, false);

      // Media queries for responsive layouts
      const smQuery = ResponsiveUtils.getFrameworkQuery(ResponsiveUtils.FRAMEWORK_QUERY_KEY.SM_ONLY);
      this.smScreen = ResponsiveKnockoutUtils.createMediaQueryObservable(smQuery);

      const navPublic = [
  { path: 'login', detail: { label: 'Login', iconClass: 'oj-ux-ico-bar-chart' } },
  { path: 'about', detail: { label: 'About', iconClass: 'oj-ux-ico-fire' } }
];

const navUser = [
  { path: 'login', detail: { label: 'Login', iconClass: 'oj-ux-ico-bar-chart' } },
  { path: 'about', detail: { label: 'About', iconClass: 'oj-ux-ico-fire' } },
  { path: 'dashboard', detail: { label: 'Dashboard', iconClass: 'oj-ux-ico-bar-chart' } },
  { path: 'portfolio', detail: { label: 'Portfolio', iconClass: 'oj-ux-ico-bar-chart' } }
];

const navAdmin = [
  { path: 'login', detail: { label: 'Login', iconClass: 'oj-ux-ico-bar-chart' } },
  { path: 'about', detail: { label: 'About', iconClass: 'oj-ux-ico-fire' } },
  { path: 'stocks', detail: { label: 'Stocks', iconClass: 'oj-ux-ico-information-s' } },
  { path: 'customers', detail: { label: 'Customers', iconClass: 'oj-ux-ico-contact-group' } },
  { path: 'transactions', detail: { label: 'Transactions', iconClass: 'oj-ux-ico-bar-chart' } }
];




      let navData = [
        { path: '', redirect: 'login' },
        { path: 'login', detail: { label: 'Login', iconClass: 'oj-ux-ico-bar-chart' } },
        { path: 'transactions', detail: { label: 'Transactions', iconClass: 'oj-ux-ico-bar-chart' } },
        { path: 'dashboard', detail: { label: 'Dashboard', iconClass: 'oj-ux-ico-bar-chart' } },
        { path: 'portfolio', detail: { label: 'Portfolio', iconClass: 'oj-ux-ico-bar-chart' } },
        { path: 'about', detail: { label: 'About', iconClass: 'oj-ux-ico-fire' } },
        { path: 'customers', detail: { label: 'Customers', iconClass: 'oj-ux-ico-contact-group' } },
        { path: 'stocks', detail: { label: 'Stocks', iconClass: 'oj-ux-ico-information-s' } }
      ];
      // Router setup
      let router = new CoreRouter(navData, {
        urlAdapter: new UrlParamAdapter()
      });
      router.sync();
      window.router=router
      this.moduleAdapter = new ModuleRouterAdapter(router);

      this.selection = new KnockoutRouterAdapter(router);

      // Setup the navDataProvider with the routes, excluding the first redirected
      // route.
       this.navDataProvider = ko.observable(new ArrayDataProvider(navPublic, {keyAttributes: "path"}));
      // Header
      var self=this;
      self.setNavForRole = function(role) {
  if (role === 'admin') {
    self.navDataProvider(new ArrayDataProvider(navAdmin, {keyAttributes: "path"}));
  } else if (role === 'user') {
    self.navDataProvider(new ArrayDataProvider(navUser, {keyAttributes: "path"}));
  } else {
    self.navDataProvider(new ArrayDataProvider(navPublic, {keyAttributes: "path"}));
  }
};
      // Application Name used in Branding Area
      this.appName = ko.observable("Stock Manager");
      // User Info used in Global Navigation area
      this.userLogin = ko.observable("Username");

       

      // Footer
      this.footerLinks = [
        {name: 'About Oracle', linkId: 'aboutOracle', linkTarget:'http://www.oracle.com/us/corporate/index.html#menu-about'},
        { name: "Contact Us", id: "contactUs", linkTarget: "http://www.oracle.com/us/corporate/contact/index.html" },
        { name: "Legal Notices", id: "legalNotices", linkTarget: "http://www.oracle.com/us/legal/index.html" },
        { name: "Terms Of Use", id: "termsOfUse", linkTarget: "http://www.oracle.com/us/legal/terms/index.html" },
        { name: "Your Privacy Rights", id: "yourPrivacyRights", linkTarget: "http://www.oracle.com/us/legal/privacy/index.html" },
      ];

// this.userLogin = ko.observable('');

// This code ensures it is updated if a user is already logged in on page load/refresh
var userObj = JSON.parse(sessionStorage.getItem('user') || '{}');
if (userObj.username) {
  this.userLogin(userObj.username);
    this.setNavForRole(userObj.custType);
}
else{
  this.setNavForRole();
}

var self=this;
self.logout = function() {
  sessionStorage.removeItem("user");
  self.userLogin('username'); // Clear username observable
  // Optionally reset other app state as needed
   self.setNavForRole();
  window.router.go({path:'login'});
};
}

     // release the application bootstrap busy state
     Context.getPageContext().getBusyContext().applicationBootstrapComplete();

     return new ControllerViewModel();
  }
);