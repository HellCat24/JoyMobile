package joy.reactor.domain.services

import joy.reactor.data.common.ioObservable
import joy.reactor.data.enteties.Profile
import joy.reactor.data.http.HttpClient
import joy.reactor.data.requests.factory.LoginRequestFactory
import joy.reactor.data.requests.factory.ProfileRequestFactory
import rx.Observable

/**
 * Created by y2k on 11/25/15.
 */
class ProfileService(
        private val profileRequestFactory: ProfileRequestFactory,
        private val loginRequestFactory: LoginRequestFactory) {

    fun login(username: String, password: String): Observable<String> {
        return loginRequestFactory.request(username, password)
    }

    fun getProfile(): Observable<Profile> {
        return profileRequestFactory.request()
    }

    fun logout(): Observable<Unit> {
        return ioObservable { HttpClient.instance.clearCookies() }
    }

    fun isAuthorized(): Observable<Boolean> {
        return profileRequestFactory
                .request()
                .map { true }.onErrorReturn { false }
    }
}