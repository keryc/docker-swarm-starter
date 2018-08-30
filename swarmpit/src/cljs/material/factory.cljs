(ns material.factory
  (:refer-clojure :exclude [stepper list])
  (:require [cljsjs.react]
            [cljsjs.material-ui]
            [cljsjs.formsy-react]
            [cljsjs.formsy-material-ui]))

(def create-factory js/React.createFactory)

;;; Material-UI

(def dialog (create-factory js/MaterialUI.Dialog))
(def avatar (create-factory js/MaterialUI.Avatar))
(def app-bar (create-factory js/MaterialUI.AppBar))
(def drawer (create-factory js/MaterialUI.Drawer))
(def snackbar (create-factory js/MaterialUI.Snackbar))
(def menu (create-factory js/MaterialUI.Menu))
(def menu-item (create-factory js/MaterialUI.MenuItem))
(def list (create-factory js/MaterialUI.List))
(def list-item (create-factory js/MaterialUI.ListItem))
(def svg-icon (create-factory js/MaterialUI.SvgIcon))
(def icon-button (create-factory js/MaterialUI.IconButton))
(def icon-menu (create-factory js/MaterialUI.IconMenu))
(def flat-button (create-factory js/MaterialUI.FlatButton))
(def raised-button (create-factory js/MaterialUI.RaisedButton))
(def toogle (create-factory js/MaterialUI.Toggle))
(def checkbox (create-factory js/MaterialUI.Checkbox))
(def linear-progress (create-factory js/MaterialUI.LinearProgress))
(def circular-progress (create-factory js/MaterialUI.CircularProgress))
(def refresh-indicator (create-factory js/MaterialUI.RefreshIndicator))
(def table (create-factory js/MaterialUI.Table))
(def table-header (create-factory js/MaterialUI.TableHeader))
(def table-header-column (create-factory js/MaterialUI.TableHeaderColumn))
(def table-body (create-factory js/MaterialUI.TableBody))
(def table-row (create-factory js/MaterialUI.TableRow))
(def table-row-column (create-factory js/MaterialUI.TableRowColumn))
(def table-footer (create-factory js/MaterialUI.TableFooter))
(def tab (create-factory js/MaterialUI.Tab))
(def tabs (create-factory js/MaterialUI.Tabs))
(def select-field (create-factory js/MaterialUI.SelectField))
(def text-field (create-factory js/MaterialUI.TextField))
(def slider (create-factory js/MaterialUI.Slider))
(def radio-button-group (create-factory js/MaterialUI.RadioButtonGroup))
(def radio-button (create-factory js/MaterialUI.RadioButton))
(def auto-complete (create-factory js/MaterialUI.AutoComplete))
(def mui-theme-provider (create-factory js/MaterialUIStyles.MuiThemeProvider))

(def mui-theme js/MaterialUIStyles.getMuiTheme)
(def fade js/MaterialUIUtils.colorManipulator.fade)

;;; Formsy

(def vform (create-factory js/Formsy.Form))

;;; Formsy Material-UI

(def vcheckbox (create-factory js/FormsyMaterialUI.FormsyCheckbox))
(def vdate (create-factory js/FormsyMaterialUI.FormsyDate))
(def vradio (create-factory js/FormsyMaterialUI.FormsyRadio))
(def vradio-group (create-factory js/FormsyMaterialUI.FormsyRadioGroup))
(def vselect (create-factory js/FormsyMaterialUI.FormsySelect))
(def vtext (create-factory js/FormsyMaterialUI.FormsyText))
(def vtime (create-factory js/FormsyMaterialUI.FormsyTime))
(def vtoogle (create-factory js/FormsyMaterialUI.FormsyToggle))
(def vauto-complete (create-factory js/FormsyMaterialUI.FormsyAutoComplete))