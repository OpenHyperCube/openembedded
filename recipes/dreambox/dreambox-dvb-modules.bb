DESCRIPTION = "Hardware drivers for Dreambox"
SECTION = "base"
PRIORITY = "required"
LICENSE = "proprietary"
MAINTAINER = "Felix Domke <tmbinc@elitedvb.net>"

KV_dm7020 = "2.6.9"
PV_dm7020 = "${KV}-20060622"

KV_dm600pvr = "2.6.12"
PV_dm600pvr = "${KV}-20090430"

KV_dm500plus = "2.6.12"
PV_dm500plus = "${KV}-20080822"

KV_dm7025 = "${@base_contains('PREFERRED_VERSION_linux-dm7025', '2.6.12.6', '2.6.12.6', '2.6.32-1.3-dm7025', d)}"
PV_dm7025 = "${KV}-${@base_contains('PREFERRED_VERSION_linux-dm7025', '2.6.12.6', '20101208', '20100727', d)}"
GCC_dm7025 = "${@base_contains('PREFERRED_VERSION_linux-dm7025', '2.6.12.6', '-gcc4.4', '', d)}"

KV_dm500hd = "${@base_contains('PREFERRED_VERSION_linux-dm500hd', '2.6.18', '2.6.18-7.4-dm500hd', '2.6.30-dm500hd', d)}"
PV_dm500hd = "${KV}-${@base_contains('PREFERRED_VERSION_linux-dm500hd', '2.6.18', '20110302', '20090727', d)}"

KV_dm800 = "${@base_contains('PREFERRED_VERSION_linux-dm800', '2.6.18', '2.6.18-7.4-dm800', '2.6.30-dm800', d)}"
PV_dm800 = "${KV}-${@base_contains('PREFERRED_VERSION_linux-dm800', '2.6.18', '20110302', '20090723', d)}"

KV_dm800_arm = '2.6.34'
PV_dm800_arm = '20130603'

KV_dm800se = "2.6.18-7.4-dm800se"
PV_dm800se = "${KV}-20110302"

KV_dm7020hd = "2.6.18-7.4-dm7020hd"
PV_dm7020hd = "${KV}-20101111"

KV_dm8000 = "${@base_contains('PREFERRED_VERSION_linux-dm8000', '2.6.18', '2.6.18-7.4-dm8000', '2.6.30-dm8000', d)}"
PV_dm8000 = "${KV}-${@base_contains('PREFERRED_VERSION_linux-dm8000', '2.6.18', '20110302', '20090820', d)}"

RDEPENDS_${PN} = "kernel (${KV}) update-modules"

RDEPENDS_${PN}_append_dm8000 = " dreambox-secondstage"
RDEPENDS_${PN}_append_dm800_mipsel = " dreambox-secondstage"
RDEPENDS_${PN}_append_dm500hd = " dreambox-secondstage"
RDEPENDS_${PN}_append_dm800se = " dreambox-secondstage"
RDEPENDS_${PN}_append_dm7020hd = " dreambox-secondstage"

MACHINE_KERNEL_PR_append = ".3"
GCC ?= ""

inherit module

do_compile() {
}

SRC_URI = "http://sources.dreamboxupdate.com/snapshots/dreambox-dvb-modules-${MACHINE}-${PV}${GCC}.tar.bz2 \
			${@base_contains("MACHINE_FEATURES", "frontprocessor", "http://sources.dreamboxupdate.com/download/7020/fpupgrade-${MACHINE}-v7", "", d)} \
			"

SRC_URI_arm = "https://www.dropbox.com/s/pcyul2wdl5jormh/su980-dvb-modules-${PV}.tar.gz"
FILES_${PN}_append_arm = " ${base_libdir}/firmware/* ${sysconfdir}/modprobe.d/vpmfbDrv.conf"
S = "${WORKDIR}"

do_install_powerpc() {
	install -d ${D}/lib/modules/${KV}/extra
	for f in head; do
		install -m 0644 $f.ko ${D}/lib/modules/${KV}/extra/$f.ko;
	done
	install -d ${D}/${sysconfdir}/modutils
	echo head > ${D}/${sysconfdir}/modutils/dreambox
}

do_install_mipsel() {
	install -d ${D}/lib/modules/${KV}/extra
	for f in *.ko LICENSE; do
		install -m 0644 ${WORKDIR}/$f ${D}/lib/modules/${KV}/extra/$f;
	done
	install -d ${D}/${sysconfdir}/modutils
	for i in `ls | grep \\.ko | sed -e 's/.ko//g'`; do
		echo $i >> ${D}/${sysconfdir}/modutils/dreambox
	done
	${@base_contains("MACHINE_FEATURES", "frontprocessor", "install -d ${D}${sbindir}; install -m 0755 ${WORKDIR}/fpupgrade-${MACHINE}-v7 ${D}${sbindir}/fpupgrade", "", d)}
}

INITSCRIPT_NAME = "populate-private-nodes.sh"
INITSCRIPT_PARAMS = "start 20 S ."
inherit update-rc.d

do_strip_modules_arm() {
}

do_install_arm() {
	install -d ${D}/lib/modules/${KV}/extra/
	for f in *.ko; do
		install -m 0644 ${WORKDIR}/$f ${D}/lib/modules/${KV}/extra/$f;
	done

	install -d ${D}/${base_libdir}/firmware
	install -m 644 ${WORKDIR}/firmware/* ${D}/${base_libdir}/firmware/

	install -d ${D}/${sysconfdir}/Wireless/RT2870STA
	install -m 644 ${WORKDIR}/RT2870STA.dat ${D}/${sysconfdir}/Wireless/RT2870STA

	install -d ${D}/${sysconfdir}/modutils
	for i in `ls | grep \\.ko | sed -e 's/.ko//g'`; do
		echo $i >> ${D}/${sysconfdir}/modutils/su980
	done

	install -d ${D}/${sysconfdir}/init.d
	install -m 755 ${WORKDIR}/${INITSCRIPT_NAME} ${D}/${sysconfdir}/init.d/

	install -d ${D}/${sysconfdir}/modprobe.d
	install -m 644 ${WORKDIR}/vpmfbDrv.conf ${D}/${sysconfdir}/modprobe.d

}
