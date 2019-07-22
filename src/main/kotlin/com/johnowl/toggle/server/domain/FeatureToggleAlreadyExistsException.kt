package com.johnowl.toggle.server.domain

import java.lang.Exception

class FeatureToggleAlreadyExistsException : Exception("Feature toggle already exists.")
