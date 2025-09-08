define(['../accUtils', 'knockout'], function (accUtils, ko) {
  function AboutViewModel() {
    var self = this;
    self.connected = () => {
      accUtils.announce('About page loaded.', 'assertive');
      document.title = "About";
    };
    self.disconnected = () => {
      // Implement if needed
    };
    self.transitionCompleted = () => {
      // Implement if needed
    };
  }
  return AboutViewModel;
});