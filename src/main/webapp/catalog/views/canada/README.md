# canada skin for GeoNetwork

Custom canada skin of GeoNetwork.

## Requirements

- GeoNetwork opensource 3.10.x

## Installation instructions

### Build time

Initialise the skin as a git submodule in `/web-ui/src/main/resources/catalog/views`

```
git submodule add -b 3.10.x https://eos.geocat.net/gitlab/canada/canada-geonetwork-ui-view.git web-ui/src/main/resources/catalog/views/canada
git submodule init
```

### Runtime (WAR)

- Deploy the latest geonetwork `3.10.x` WAR from [Sourceforge](https://sourceforge.net/projects/geonetwork/files/GeoNetwork_opensource/)
- Grab a zip of https://eos.geocat.net/gitlab/canada/canada-geonetwork-ui-view.git
- Unzip it in `/geonetwork/catalog/views/canada`

## Settings

### User Interface

In `Admin` > `Settings` > `User Interface` select the option to show the logo in the Header

Change the width of the page to a fixed width:

- Go to  `Settings` > `User Interface` > `Top toolbar` > `Fluid container for Header and Top menu` and uncheck the option
- Go to  `Settings` > `User Interface` > `Home page` > `Fluid container for Home and Search` and uncheck the option

Do not change:

- Editor width: Go to  `Settings` > `User Interface` > `Editor application` > `Fluid container for the Editor` and make shure the option is checked

---

** TIP**

To easily find the options, start by typing `Fluid` in filter box on top of the `User Interface` page