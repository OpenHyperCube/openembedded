require cmake.inc

inherit cmake

do_configure_append() {
	sed -i -e 's,HAVE_GLIBC_STRERROR_R__TRYRUN_OUTPUT-ADVANCED:INTERNAL=1,HAVE_GLIBC_STRERROR_R__TRYRUN_OUTPUT-ADVANCED:INTERNAL=0,' CMakeCache.txt
}

SRC_URI += "file://fix_fortify_source_compilation.patch"
SRC_URI[md5sum] = "50f387d0436696c4a68b5512a72c9cde"
SRC_URI[sha256sum] = "9cdd2152e37b05d0d40d334a1bb2dfc0250021797360f971c6ea3d457ac9fdf2"
